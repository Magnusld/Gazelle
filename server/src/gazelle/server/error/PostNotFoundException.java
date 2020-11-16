package gazelle.server.error;

import org.springframework.http.HttpStatus;

public class PostNotFoundException extends GazelleException {

    public PostNotFoundException() {
        super("Post not found", HttpStatus.NOT_FOUND);
    }
}
