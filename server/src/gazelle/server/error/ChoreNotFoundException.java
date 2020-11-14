package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class ChoreNotFoundException extends GazelleException {

    public ChoreNotFoundException() {
        super("Chore not found", HttpStatus.NOT_FOUND);
    }
}
