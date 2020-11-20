package gazelle.api.auth;

import gazelle.api.UserResponse;
import gazelle.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTest {

    @Test
    public void logInRequest() {
        String email = "jrgen@jrgensen.no";
        String password = "ikkjeSikkert";
        LogInRequest request = new LogInRequest(email, password);

        assertEquals(request.getEmail(), email);
        request.setEmail("jrgentest@jrgensentest.no");
        assertEquals(request.getEmail(), "jrgentest@jrgensentest.no");
        
        assertEquals(request.getPassword(), password);
        request.setPassword("littMeirSikkert123");
        assertEquals(request.getPassword(), "littMeirSikkert123");

        assertEquals(request, request);
        assertNotEquals(request, null);
    }

    @Test
    public void logInResponse() {
        String token = "Bearer: validx";
        UserResponse.Builder builder = new UserResponse.Builder();
        builder.id((long) 11).firstName("Per").lastName("Johansen");
        UserResponse user = builder.build();

        LogInResponse logInResponse = new LogInResponse(token, user);
        assertEquals(token, logInResponse.getToken());

        logInResponse.setToken("Bearer: xvalidy");
        assertNotEquals(token, logInResponse.getToken());
        assertEquals(logInResponse.getToken(), "Bearer: xvalidy");

        assertEquals(user, logInResponse.getUser());

        UserResponse.Builder builder2 = new UserResponse.Builder();
        builder2.id((long) 12).firstName("Petter").lastName("Johnson");
        UserResponse user2 = builder2.build();
        logInResponse.setUser(user2);
        assertEquals(logInResponse.getUser(), user2);
        assertNotEquals(user, logInResponse.getUser());

        assertEquals(logInResponse, logInResponse);
        assertNotEquals(logInResponse, null);
    }

    @Test
    public void signUpRequest() {
        String firstname = "Karsten";
        String lastname = "Warholm";
        String email = "karsten@dimna.no";
        String password = "egerverdsmeister";
        SignUpRequest signUpRequest =
                new SignUpRequest(firstname, lastname, email, password);

        assertEquals(signUpRequest.getFirstname(), firstname);
        signUpRequest.setFirstname("Karstein");
        assertEquals(signUpRequest.getFirstname(), "Karstein");

        assertEquals(signUpRequest.getLastname(), lastname);
        signUpRequest.setLastname("Varholm");
        assertEquals(signUpRequest.getLastname(), "Varholm");

        assertEquals(signUpRequest.getEmail(), email);
        signUpRequest.setEmail("karsten@oslo.no");
        assertEquals(signUpRequest.getEmail(), "karsten@oslo.no");

        assertEquals(signUpRequest.getPassword(), password);
        signUpRequest.setPassword("eglikarlego");
        assertEquals(signUpRequest.getPassword(), "eglikarlego");

        User user = signUpRequest.buildUser();

        assertEquals(signUpRequest, signUpRequest);
        assertNotEquals(signUpRequest, null);
    }

    @Test
    public void userFromTokenRequest() {
        UserFromTokenRequest user = new UserFromTokenRequest("Bearer: validx");
        assertEquals(user.getToken(), "Bearer: validx");
        user.setToken("Bearer: xvalidy");
        assertNotEquals(user.getToken(), "Bearer: validx");
        assertEquals(user.getToken(), "Bearer: xvalidy");
    }
}
