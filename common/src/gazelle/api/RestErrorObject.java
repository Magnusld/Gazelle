package gazelle.api;

/**
 * Object returned when certain exceptions are thrown in RestControllers.
 * Will ignore null fields.
 */
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
