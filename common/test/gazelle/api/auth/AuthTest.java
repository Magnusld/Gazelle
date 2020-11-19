package gazelle.api.auth;

import gazelle.api.UserResponse;
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
        assertEquals(request.getEmail(),"jrgentest@jrgensentest.no");
        
        assertEquals(request.getPassword(), password);
        request.setPassword("littMeirSikkert123");
        assertEquals(request.getPassword(), "littMeirSikkert123");

        assertTrue(request.equals(request));
        assertFalse(request.equals(null));
    }

    @Test
    public void logInResponse() {
        String token = "Bearer: validx";
        UserResponse userResponse = null;
        userResponse.setId((long) 11);
        
    }

    @Test
    public void logOutRequest() {
        String token = "Bearer: validx";
        LogOutRequest logOutRequest = new LogOutRequest(token);

        assertEquals(logOutRequest.getToken(), token);

        logOutRequest.setToken("Bearer: xvalidy");
        assertFalse(logOutRequest.getToken().equals(token));
        assertEquals(logOutRequest.getToken(), "Bearer: xvalidy");

        assertTrue(logOutRequest.equals(logOutRequest));
        assertFalse(logOutRequest.equals(null));
    }


}
