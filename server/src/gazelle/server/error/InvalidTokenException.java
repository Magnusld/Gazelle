package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends GazelleException {
    public InvalidTokenException() {
        super("invalid authorization token", null, HttpStatus.UNAUTHORIZED);
    }
}
