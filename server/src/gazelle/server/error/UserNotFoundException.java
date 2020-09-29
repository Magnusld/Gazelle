package gazelle.server.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class UserNotFoundException extends GazelleException {

    public UserNotFoundException() {
        super("User not found", null, HttpStatus.NOT_FOUND);
    }
}
