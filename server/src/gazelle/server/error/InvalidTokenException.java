package gazelle.server.error;

import org.springframework.http.HttpStatus;

/**
 * Thrown when:
 *  - Authorization header doesn't start with "Bearer "
 *  - Token is not recognized in database
 *
 * <p>Not thrown when:
 *  - Authorization header is missing
 *  - Token belongs to a user, but a user without permission
 */
public class InvalidTokenException extends GazelleException {
    public InvalidTokenException() {
        this(null);
    }

    public InvalidTokenException(String message) {
        super("invalid authorization token", message, HttpStatus.UNAUTHORIZED);
    }
}
