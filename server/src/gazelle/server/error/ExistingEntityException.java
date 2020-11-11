package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class ExistingEntityException extends GazelleException {

    public ExistingEntityException() {
        super("Entity already exists", HttpStatus.CONFLICT);
    }
}
