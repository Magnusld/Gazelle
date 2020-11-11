package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends GazelleException {

    public UserNotFoundException() {
        super("User not found", HttpStatus.NOT_FOUND);
    }
}
