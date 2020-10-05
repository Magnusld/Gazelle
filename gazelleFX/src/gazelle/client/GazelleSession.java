package gazelle.client;

import gazelle.auth.*;
import gazelle.model.Course;
import gazelle.model.User;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

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

    /**
     * Creates a new session, without a logged in user
     * @param url the server URL, including protocol
     */
    public GazelleSession(String url) {
        client = ClientBuilder.newClient();
        apiRoot = client.target(url);
    }

    // Make a path object relative to apiRoot
    private WebTarget path(String path) {
        return apiRoot.path(path);
    }

    private Invocation.Builder unauthorizedPath(WebTarget path) {
        return path.request(MediaType.APPLICATION_JSON);
    }

    private Invocation.Builder unauthorizedPath(String pathname) {
        return unauthorizedPath(path(pathname));
    }

    private Invocation.Builder authorizedPath(WebTarget path) {
        if (token == null)
            throw new IllegalStateException("Not logged in to GazelleSession");
        return unauthorizedPath(path).header("Authorization", "Bearer " + token);
    }

    private Invocation.Builder authorizedPath(String pathname) {
        return authorizedPath(path(pathname));
    }

    /**
     * Create a new User object and try signing it up on the server.
     * You do not need to be logged in.
     * @param username the desired username
     * @param password the desired password
     * @throws SignUpException for a reason described in the SignUpException.getReason() enum
     * @throws ClientException if request fails in some other way
     */
    public void signUp(String username, String password) {
        if (isLoggedIn())
            logOut();

        SignUpRequest request = new SignUpRequest(username, password);

        Response response = unauthorizedPath("signup").post(Entity.json(request));
        if (response.getStatusInfo() == Response.Status.CONFLICT)
            throw new SignUpException(SignUpException.Reason.USERNAME_TAKEN);
        if (response.getStatus() == 422) //UNPROCESSABLE_ENTITY
            throw new SignUpException(SignUpException.Reason.PASSWORD_BAD);
        if (response.getStatusInfo() != Response.Status.OK)
            throw new ClientException("Failed to sign up", response);

        LogInResponse logInResponse = response.readEntity(LogInResponse.class);
        this.token = logInResponse.getToken();
        this.loggedInUser = logInResponse.getUser();
    }

    /**
     * Attempts to log in with username and password.
     * If successful, receives a token and user object from the server.
     * @throws LogInException if status code is 401 (UNAUTHORIZED)
     * @throws ClientException for all other non-200 status codes
     * @throws javax.ws.rs.ProcessingException if the response is missing/malformed
     */
    public void logIn(String username, String password) {
        if (isLoggedIn())
            logOut();

        LogInRequest request = new LogInRequest(username, password);

        // We always use POST when sending secrets
        Response response = unauthorizedPath("login").post(Entity.json(request));
        if (response.getStatusInfo() == Response.Status.UNAUTHORIZED)
            throw new LogInException();
        if (response.getStatusInfo() != Response.Status.OK)
            throw new ClientException("Failed to log in", response);

        LogInResponse logInResponse = response.readEntity(LogInResponse.class);
        this.token = logInResponse.getToken();
        this.loggedInUser  = logInResponse.getUser();
    }

    /**
     * Attempts to log in with a previously received token.
     * If successful the object representing the logged in user
     * will be received and stored in the session.
     * @throws LogInException if status code is 401 (UNAUTHORIZED)
     * @throws ClientException if all other non-200 status codes
     * @throws javax.ws.rs.ProcessingException if the response is missing/malformed
     */
    public void logIn(String token) {
        if (isLoggedIn())
            logOut();

        UserFromTokenRequest request = new UserFromTokenRequest(token);

        // We always use POST when sending secrets
        Response response = unauthorizedPath("users/token").post(Entity.json(request));
        if (response.getStatusInfo() != Response.Status.OK)
            throw new ClientException("Failed to get User from token", response);

        User user = response.readEntity(User.class);

        this.token = token;
        this.loggedInUser = user;
    }

    /**
     * Tells the server to invalidate the token and sets
     * the token and logged in user to null.
     * @throws IllegalStateException if client not logged in
     * @throws ClientException if request fails
     */
    public void logOut() {
        LogOutRequest request = new LogOutRequest(token);

        // We always use POST when sending secrets
        Response response = authorizedPath("logout").post(Entity.json(request));

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

    /**
     * @return the User object for the currently logged in user, or null
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Tries to create a new User on the server.
     * You do not need to be logged in to call it.
     *
     * <p>Will fail e.g. if username is taken.
     *
     * @param username the wanted username
     * @param password the wanted password
     * @return the newly created User
     * @throws ClientException if request fails
     */
    public User addNewUser(String username, String password) {
        User newUser = new User(username, password);

        Response response = unauthorizedPath("users").post(Entity.json(newUser));
        if (response.getStatusInfo() != Response.Status.OK)
            throw new ClientException("Failed to create user", response);

        return response.readEntity(User.class);
    }

    /**
     * Gets a list of all courses either followed or owned by the user.
     *
     * @param user the User object
     * @return all courses owner or followed by the user
     * @throws ClientException if request fails
     */
    public List<Course> getCoursesForUser(User user) {
        WebTarget path = path("courses/user").queryParam("userId", user.getId());
        Response response = authorizedPath(path).get();

        if (response.getStatusInfo() != Response.Status.OK)
            throw new ClientException("Failed to get courses for user", response);

        // This is how we specify what T is when receiving a List<T>
        // https://stackoverflow.com/questions/35313767/how-to-get-liststring-as-response-from-jersey2-client
        List<Course> courses = response.readEntity(new GenericType<List<Course>>() {});
        return courses;
    }
}
