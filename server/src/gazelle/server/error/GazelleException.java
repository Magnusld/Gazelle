package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class GazelleException extends RuntimeException {

    private final String reason;
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
     * @param reason The reason given to the client for the failed request, or null
     * @param message An additional message given to the client, or null
     * @param statusCode The Http status code of the response
     */
    public GazelleException(String reason,  String message, HttpStatus statusCode) {
        super(message);
        this.reason = reason;
        this.statusCode = statusCode;
    }

    public String getReason() {
        return this.reason;
    }

    public RestErrorObject buildErrorObject() {
        return new RestErrorObject(getReason(), getMessage());
    }

    public HttpStatus getStatusCode() {
        return this.statusCode;
    }
}
