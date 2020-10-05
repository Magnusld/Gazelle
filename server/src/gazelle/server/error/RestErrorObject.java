package gazelle.server.error;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Object returned when certain exceptions are thrown in RestControllers.
 * Will ignore null fields.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestErrorObject {
    /**
     * The reason for the error, most likely an exception.
     * May be null.
     */
    private final String reason;

    /**
     * The message associated with the error, possibly describing why it was thrown.
     * May be null.
     */
    private final String message;

    public RestErrorObject(String reason, String message) {
        this.reason = reason;
        this.message = message;
    }

    public String getReason() {
        return reason;
    }

    public String getMessage() {
        return message;
    }
}
