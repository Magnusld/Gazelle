package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class LoginFailedException extends GazelleException {
    public LoginFailedException() {
        super("Login failed", null, HttpStatus.UNAUTHORIZED);
    }
}
