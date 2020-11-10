package gazelle.server.error;

import org.springframework.http.HttpStatus;

/**
 * Thrown when:
 *  - The user didn't provide any Authorization-header
 *
 *  <p>(aka when token is null)
 */
public class MissingAuthorizationException extends GazelleException {

    public MissingAuthorizationException() {
        super("Missing 'Authorization'-header", HttpStatus.UNAUTHORIZED);
    }
}
