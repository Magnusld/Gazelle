package gazelle.server.error;

import org.springframework.http.HttpHeaders;
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

    @ExceptionHandler({GazelleException.class})
    public ResponseEntity<Object> handleNotFound(
            GazelleException gex, WebRequest request) {
        return handleExceptionInternal(gex, gex.buildErrorObject(),
                new HttpHeaders(), gex.getStatusCode(), request);
    }
}