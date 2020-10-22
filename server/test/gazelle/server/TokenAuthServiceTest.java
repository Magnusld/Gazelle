package gazelle.server;

import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.endpoint.LoginEndpoint;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenAuthServiceTest {

    @Autowired
    private LoginEndpoint loginEndpoint;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private String user1Token;
    private User user2;
    private String user2Token;

    @BeforeAll
    public void createUsers() {
        LogInResponse r1 = loginEndpoint.signup(new SignUpRequest("TAST1", "1356394"));
        user1 = r1.getUser();
        user1Token = TokenAuthService.addBearer(r1.getToken());
        LogInResponse r2 = loginEndpoint.signup(new SignUpRequest("TAST2", "1356394"));
        user2 = r2.getUser();
        user2Token = TokenAuthService.addBearer(r2.getToken());
    }

    @Test
    public void testAssertLoggedIn() {
        assertEquals(user1, tokenAuthService.assertUserLoggedIn(user1.getId(), user1Token));
        tokenAuthService.assertUserLoggedIn(user2, user2Token);

        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.assertUserLoggedIn(user1, "not a token");
        });
        assertThrows(AuthorizationException.class, () -> {
            tokenAuthService.assertUserLoggedIn(user1, user2Token);
        });
    }

    @Test
    public void testGetUserForToken() {
        assertEquals(tokenAuthService.getUserForToken(user1Token), user1);
        assertEquals(tokenAuthService.getUserForToken(user2Token), user2);

        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserForToken("not a token");
        });
    }

    @Test
    public void testCreateTokenForUser() {
        String oldUser1Token = user1Token;
        user1Token = TokenAuthService.addBearer(tokenAuthService.createTokenForUser(user1));
        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserForToken(oldUser1Token);
        });
        assertEquals(user1, tokenAuthService.getUserForToken(user1Token));
    }

    @Test
    public void testDeleteToken() {
        assertEquals(user1, tokenAuthService.getUserForToken(user1Token));
        tokenAuthService.removeToken(user1Token);
        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserForToken(user1Token);
        });
        user1Token = TokenAuthService.addBearer(tokenAuthService.createTokenForUser(user1));
        assertEquals(user1, tokenAuthService.getUserForToken(user1Token));
    }

    @AfterAll
    public void deleteUsers() {
        userRepository.deleteById(user1.getId());
        userRepository.deleteById(user2.getId());
    }
}
