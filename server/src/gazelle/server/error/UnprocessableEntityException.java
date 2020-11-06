package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends GazelleException {

    public UnprocessableEntityException(String message) {
        super("Unprocessable entity", message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public UnprocessableEntityException() {
        this(null);
    }
}
