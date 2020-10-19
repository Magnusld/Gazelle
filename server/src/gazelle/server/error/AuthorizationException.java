package gazelle.server.error;

import org.springframework.http.HttpStatus;

/**
 * Thrown if:
 *  - The logged in user doesn't have permission to do the attempted operation
 */
public class AuthorizationException extends GazelleException {
    public AuthorizationException() {
        super("Unauthorized", "The logged in user does not have permission", HttpStatus.UNAUTHORIZED);
    }
}
