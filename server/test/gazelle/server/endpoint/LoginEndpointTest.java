package gazelle.server.endpoint;

import gazelle.api.UserResponse;
import gazelle.api.auth.LogInRequest;
import gazelle.api.auth.LogInResponse;
import gazelle.api.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.error.*;
import gazelle.server.service.TokenAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginEndpointTest {

    @Autowired
    private LoginEndpoint loginEndpoint;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private TestHelper testHelper;

    @Test
    public void signup() {
        UnprocessableEntityException uex = assertThrows(UnprocessableEntityException.class, () -> {
            loginEndpoint.signup(
                    new SignUpRequest("Hallvard", "Trætteberg",
                            "hallemann@nrk.no", "123"));
        });

        final String FIRSTNAME = "testfirst";
        final String LASTNAME = "testlast";
        final String EMAIL = "testemail";
        final String PASS = "testpass";

        LogInResponse response = loginEndpoint.signup(
                new SignUpRequest(FIRSTNAME, LASTNAME, EMAIL, PASS));
        UserResponse user = response.getUser();
        String token = TokenAuthService.addBearer(response.getToken());

        assertEquals(user.getId(), tokenAuthService.getUserIdFromToken(token));

        GazelleException gex = assertThrows(GazelleException.class, () -> {
            loginEndpoint.signup(new SignUpRequest("dummy", "dummy", EMAIL, "dummy"));
        });
        assertEquals("Email taken", gex.getMessage());
        assertEquals(HttpStatus.CONFLICT, gex.getStatusCode());

        testHelper.deleteTestUser(user.getId());
    }

    @Test
    public void login() {
        User user = testHelper.createTestUserObject();
        assertThrows(LoginFailedException.class, () -> {
            loginEndpoint.login(new LogInRequest("dummy username", "dummy password"));
        });
        assertThrows(LoginFailedException.class, () -> {
            loginEndpoint.login(new LogInRequest(user.getEmail(), "dummy password"));
        });

        LogInResponse response = loginEndpoint.login(
                new LogInRequest(user.getEmail(), user.getPassword()));

        assertEquals(user.getId(), response.getUser().getId());
        String token = TokenAuthService.addBearer(response.getToken());
        assertEquals(user.getId(), tokenAuthService.getUserIdFromToken(token));

        testHelper.deleteTestUser(user);
    }

    @Test
    public void loginWithToken() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);
        assertThrows(InvalidTokenException.class, () -> {
            loginEndpoint.loginWithToken("Bearer: dummy");
        });
        assertEquals(user, loginEndpoint.loginWithToken(token).getId());
        testHelper.deleteTestUser(user);
    }

    @Test
    public void logout() {
        User user = testHelper.createTestUserObject();
        String token = testHelper.logInUser(user.getId());

        assertThrows(MissingAuthorizationException.class, () -> {
            loginEndpoint.logout(null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            loginEndpoint.logout("Bearer: dummy");
        });
        loginEndpoint.logout(token);

        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserIdFromToken(token);
        });

        testHelper.deleteTestUser(user);
    }
}
