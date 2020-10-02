package gazelle.client;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.auth.LogOutRequest;
import gazelle.auth.UserFromTokenRequest;
import gazelle.model.User;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * A class representing the connection with the server.
 * Has a few functions that work without being logged in.
 * Will keep track of token and User once logged in,
 *
 * <p>Loosely based on:
 * https://www.baeldung.com/jersey-jax-rs-client
 */
public class GazelleSession {

    private Client client;
    private WebTarget apiRoot;

    // The object representing the logged in user on the server
    private User loggedInUser;
    // The token sent with every authenticated request
    private String token;

    public GazelleSession(String url) {
        client = ClientBuilder.newClient();
        apiRoot = client.target(url);
    }

    private Invocation.Builder unauthorizedPath(String path) {
        return apiRoot
                .path("login")
                .request(MediaType.APPLICATION_JSON);
    }

    private Invocation.Builder path(String path) {
        if (token == null)
            throw new IllegalStateException("Not logged in to GazelleSession");
        return unauthorizedPath(path).header("Authorization", "Bearer " + token);
    }

    /**
     * Attempts to log in with username and password.
     * If successful, receives a token and user object from the server.
     * @return true if login was successful
     */
    public boolean logIn(String username, String password) {
        if (isLoggedIn())
            logOut();

        LogInRequest request = new LogInRequest(username, password);

        // We always use POST when sending secrets
        Response response = unauthorizedPath("login").post(Entity.json(request));
        if (response.getStatusInfo() != Response.Status.OK)
            return false; //TODO: Explain why logging in failed

        LogInResponse logInResponse = response.readEntity(LogInResponse.class);
        this.token = logInResponse.getToken();
        this.loggedInUser  = logInResponse.getUser();
        return true;
    }

    /**
     * Attempts to log in with a previously received token.
     * If successful the object representing the logged in user
     * will be received and stored in the session.
     * @return true if login was successful
     */
    public boolean logIn(String token) {
        if (isLoggedIn())
            logOut();

        UserFromTokenRequest request = new UserFromTokenRequest(token);

        // We always use POST when sending secrets
        Response response = unauthorizedPath("user/token").post(Entity.json(request));
        if (response.getStatusInfo() != Response.Status.OK)
            return false;

        User user = response.readEntity(User.class);

        this.token = token;
        this.loggedInUser = user;
        return true;
    }

    /**
     * Tells the server to invalidate the token and sets
     * the token and logged in user to null.
     * @throws IllegalStateException if client not logged in
     * @throws ClientException if return code isn't 200
     */
    public void logOut() {
        LogOutRequest request = new LogOutRequest(token);

        // We always use POST when sending secrets
        Response response = path("logout").post(Entity.json(request));

        token = null;
        loggedInUser = null;

        if (response.getStatusInfo() != Response.Status.OK)
            throw new ClientException("Failed to log out", response);
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
}
