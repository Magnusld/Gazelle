package gazelle.client;

import gazelle.api.RestErrorObject;
import gazelle.api.UserResponse;
import gazelle.client.error.ClientException;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Objects;

/**
 * A class representing the connection with the server.
 * Has a few functions that work without being logged in.
 * Will keep track of token and User once logged in,
 *
 * <p>Loosely based on:
 * https://www.baeldung.com/jersey-jax-rs-client
 */
public class GazelleSession {

    private final Client client;
    private WebTarget apiRoot;

    // The object representing the logged in user on the server
    @Nullable
    private UserResponse loggedInUser;
    // The token sent with every authenticated request
    private String token;

    /**
     * Creates a new session, without a logged in user
     * @param baseURL the server URL, including protocol
     */
    public GazelleSession(String baseURL) {
        client = ClientBuilder.newClient();
        apiRoot = client.target(baseURL);
    }

    /**
     * Sets the baseURL for all other requests
     * @param baseURL the server URL, including protocol
     */
    public void setBaseURL(String baseURL) {
        apiRoot = client.target(baseURL);
    }

    /**
     * Makes a WebTarget object relative to the baseURL
     * @param path the URL after baseURL/
     * @return WebTarget the webTarget
     */
    public WebTarget path(String path) {
        return apiRoot.path(path);
    }

    public Invocation.Builder unauthorizedPath(WebTarget path) {
        return path.request(MediaType.APPLICATION_JSON);
    }

    public Invocation.Builder unauthorizedPath(String pathname) {
        return unauthorizedPath(path(pathname));
    }

    public Invocation.Builder authorizedPath(WebTarget path) {
        if (token == null)
            throw new IllegalStateException("Not logged in to GazelleSession");
        return unauthorizedPath(path).header("Authorization", "Bearer " + token);
    }

    public Invocation.Builder authorizedPath(String pathname) {
        return authorizedPath(path(pathname));
    }

    /**
     * Finds out if the session is logged in or not.
     * Begin logged in means having a token and a User object representing you.
     *
     * <p>Note: Since authenticating, the token may no longer be valid on the server.
     * @return true if the session has a token and a logged in user
     */
    public boolean isLoggedIn() {
        return loggedInUser != null && token != null;
    }

    public void setLoggedIn(String token, UserResponse user) {
        Objects.requireNonNull(token);
        Objects.requireNonNull(user);
        this.token = token;
        this.loggedInUser = user;
    }

    public void setLoggedOut() {
        this.token = null;
        this.loggedInUser = null;
    }

    /**
     * @return the User object for the currently logged in user, or null
     */
    @Nullable
    public UserResponse getLoggedInUser() {
        return loggedInUser;
    }
}
