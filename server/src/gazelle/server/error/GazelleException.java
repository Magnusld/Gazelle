package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class GazelleException extends RuntimeException {

    private final HttpStatus statusCode;

    /**
     * A generic exception for any problem that prevents a REST-endpoint
     * from returning actual data.
     *
     * <p>In such cases the client should instead receive a Http status code
     * in the 4xx-5xx-range.
     *
     * <p>The client still expects the response to follow the Allow-header,
     * which means we must provide Spring with an object that can be serialized.
     * This object contains reason and message, unless they are null.
     *
     * @param message A message given to the client, or null
     * @param statusCode The HTTP status code of the response
     */
    public GazelleException(String message, HttpStatus statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public RestErrorObject buildErrorObject() {
        return new RestErrorObject(getMessage());
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
