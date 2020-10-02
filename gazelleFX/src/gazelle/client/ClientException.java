package gazelle.client;

import javax.ws.rs.core.Response;

public class ClientException extends RuntimeException {
    public ClientException() {}

    public ClientException(String message) {
        super(message);
    }

    public ClientException(String message, int statusCode, String content) {
        this(String.format("%s\nCode: %d\nContent: %s", message, statusCode, content));
    }

    public ClientException(String message, Response response) {
        this(message, response.getStatus(), response.readEntity(String.class));
    }
}
