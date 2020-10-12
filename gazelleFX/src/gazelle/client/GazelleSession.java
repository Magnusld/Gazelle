package gazelle.client;

import gazelle.auth.*;
import gazelle.client.error.ClientException;
import gazelle.client.error.LogInException;
import gazelle.client.error.SignUpException;
import gazelle.model.Course;
import gazelle.model.CourseRole.CourseRoleType;
import gazelle.model.User;
import org.jetbrains.annotations.Nullable;

import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
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
    private final WebTarget apiRoot;

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
        if (!successfulPostWithEntity(response.getStatusInfo()))
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
        if (!successfulPostWithEntity(response.getStatusInfo()))
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
        if (!successfulPostWithEntity(response.getStatusInfo()))
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

        if (!successfulPost(response.getStatusInfo()))
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
    @Nullable
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Gets a list of all courses associated with a user.
     *
     * @param user the User object
     * @param roleFilter if non-null: only return courses where the user has this role
     * @return all courses owner or followed by the user
     * @throws ClientException if request fails
     */
    public List<Course> getCoursesForUser(User user, @Nullable CourseRoleType roleFilter) {
        WebTarget path = path("users/{userId}/courses")
                .resolveTemplate("userId", user.getId());
        if (roleFilter != null)
            path = path.queryParam("role", roleFilter);
        Response response = authorizedPath(path).get();

        if (!successfulGet(response.getStatusInfo()))
            throw new ClientException("Failed to get courses for user", response);

        // This is how we specify what T is when receiving a List<T>
        // https://stackoverflow.com/questions/35313767/how-to-get-liststring-as-response-from-jersey2-client
        return response.readEntity(new GenericType<List<Course>>() {});
    }

    /**
     * Upload a new course to the server
     * @param course the new course
     * @return the new course with the id on the server
     * @throws ClientException if the server does not return 200
     * @throws IllegalArgumentException if the course is not new
     */
    public Course postNewCourse(Course course) {
        Objects.requireNonNull(course);
        if (course.getId() != null)
            throw new IllegalArgumentException("A new course can't already have an id");
        Response response = authorizedPath("courses").post(Entity.json(course));

        if (!successfulPostWithEntity(response.getStatusInfo()))
            throw new ClientException("Failed to post new course", response);

        return response.readEntity(Course.class);
    }

    /**
     * Tell the server to assign a user to a specific role in a course.
     * Will override any existing role between them.
     * If role is null will delete the relationship.
     * If the role didn't exist, attempting do delete it will cause a ClientException
     * @param user the user to assign role to
     * @param course the course in question
     * @param role the desired role, or null to remove
     * @throws ClientException if the request failed
     */
    public void setUserRoleForCourse(User user, Course course, @Nullable CourseRoleType role) {
        WebTarget path = path("users/{userId}/courses/{courseId}/role")
                .resolveTemplate("userId", user.getId())
                .resolveTemplate("courseId", course.getId());

        if (role != null) {
            Response response = authorizedPath(path).put(Entity.json(role));
            if (!successfulPut(response.getStatusInfo()))
                throw new ClientException("Failed to set role for user for course", response);
        } else {
            Response response = authorizedPath(path).delete();
            if (!successfulDelete(response.getStatusInfo()))
                throw new ClientException("Failed to delete role for user for course", response);
        }
    }

    /**
     * Checks if the status code corresponds with a successful GET request.
     * Only 200 (OK) is seen as success.
     * @param status the response status
     * @return true if the response was successful
     */
    private static boolean successfulGet(Response.StatusType status) {
        return status.equals(Response.Status.OK);
    }

    /**
     * Checks if the status code corresponds with a successful POST request.
     * 201 (Created) means the URI for the created object is in the Location header.
     * 200 (OK) and 204 (No content) mean the object was created, and in the first case, returned.
     * @param status the response status
     * @return true if the response was successful
     */
    private static boolean successfulPost(Response.StatusType status) {
        return status.equals(Response.Status.OK)
                || status.equals(Response.Status.CREATED)
                || status.equals(Response.Status.NO_CONTENT);
    }

    /**
     * Checks if the status code corresponds with a successful POST request with
     * the newly created object included in the body of the response.
     * @param status the response status
     * @return true if the status indicates that the created object is in the response body
     */
    private static boolean successfulPostWithEntity(Response.StatusType status) {
        return status.equals(Response.Status.OK);
    }

    /**
     * Checks if the status code corresponds with a successful PUT request.
     * If the object is created the status is 201 (Created).
     * If the object already existed and is replaced,
     *   the status code is 200 (OK) or 204 (No content).
     * @param status the response status
     * @return true if the response was successful
     */
    private static boolean successfulPut(Response.StatusType status) {
        return status.equals(Response.Status.OK)
                || status.equals(Response.Status.CREATED)
                || status.equals(Response.Status.NO_CONTENT);
    }

    /**
     * Checks if the status code corresponds with a successful DELETE request.
     * 200 (OK) means the object is deleted and the response contains an entity.
     * 202 (Accepted) means the object is queued for eventual deletion.
     * 204 (No Content) means the object is deleted but no other info is returned.
     * @param status the response status
     * @return true if the response was successful
     */
    private static boolean successfulDelete(Response.StatusType status) {
        return status.equals(Response.Status.OK)
                || status.equals(Response.Status.ACCEPTED)
                || status.equals(Response.Status.NO_CONTENT);
    }
}
