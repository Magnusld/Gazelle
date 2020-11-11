package gazelle.server.error;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Object returned when certain exceptions are thrown in RestControllers.
 * Will ignore null fields.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestErrorObject {
    /**
     * The message associated with the error, possibly describing why it was thrown.
     * May be null.
     */
    private final String message;

    public RestErrorObject(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
