package gazelle.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import gazelle.server.endpoint.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Based on
 * https://www.baeldung.com/spring-boot-start
 *
 * <p>Handles certain exceptions thrown in RestControllers
 * and determines what the response and status code should be instead.
 *
 * <p>We create an object for the error,
 * which will then be serialized in a way that
 * respects the client's Accept-header.
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handleNotFound(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, new ErrorObject(ex),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    /**
     * Object returned when certain exceptions are thrown in RestControllers.
     * Will ignore null fields.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ErrorObject {

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

        public ErrorObject(Exception e) {
            this.reason = e.getClass().getName();
            this.message = e.getMessage();
        }

        public ErrorObject(String reason, String message) {
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
}