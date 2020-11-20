package gazelle.client;

import gazelle.api.RestErrorObject;
import gazelle.api.UserResponse;
import gazelle.api.auth.*;
import gazelle.client.error.ClientException;
import gazelle.client.error.LogInException;
import gazelle.client.error.SignUpException;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class LoginClient {
    private final GazelleSession session;

    public LoginClient(GazelleSession session) {
        this.session = session;
    }

    /**
     * Create a new User object and try signing it up on the server.
     * You do not need to be logged in.
     * @param firstname the User's first name
     * @param lastname the User's last name
     * @param email the User's email
     * @param password the desired password
     * @throws SignUpException for a reason described in the SignUpException.getReason() enum
     * @throws ClientException if request fails in some other way
     */
    public void signUp(String firstname, String lastname, String email, String password) {
        if (session.isLoggedIn())
            logOut();

        SignUpRequest request = new SignUpRequest(firstname, lastname, email, password);

        Response response = session.unauthorizedPath("signup").post(Entity.json(request));
        if (response.getStatusInfo().equals(Response.Status.CONFLICT)
                || response.getStatus() == 422) //Unprocessable entity
            throw new SignUpException(response.readEntity(RestErrorObject.class).getMessage());
        if (!ResponseCode.successfulPostWithEntity(response.getStatusInfo()))
            throw new ClientException("Failed to sign up", response);

        LogInResponse logInResponse = response.readEntity(LogInResponse.class);
        session.setLoggedIn(logInResponse.getToken(), logInResponse.getUser());
    }

    /**
     * Attempts to log in with username and password.
     * If successful, receives a token and user object from the server.
     * @param email the e-mail the User is registered with
     * @param password the password to log in with
     * @throws LogInException if status code is 401 (UNAUTHORIZED)
     * @throws ClientException for all other non-200 status codes
     * @throws javax.ws.rs.ProcessingException if the response is missing/malformed
     */
    public void logIn(String email, String password) {
        if (session.isLoggedIn())
            logOut();

        LogInRequest request = new LogInRequest(email, password);

        // We always use POST when sending secrets
        Response response = session.unauthorizedPath("login").post(Entity.json(request));
        if (response.getStatusInfo().equals(Response.Status.UNAUTHORIZED))
            throw new LogInException();
        if (!ResponseCode.successfulPostWithEntity(response.getStatusInfo()))
            throw new ClientException("Failed to log in", response);

        LogInResponse logInResponse = response.readEntity(LogInResponse.class);
        session.setLoggedIn(logInResponse.getToken(), logInResponse.getUser());
    }

    /**
     * Attempts to log in with a previously received token.
     * If successful the object representing the logged in user
     * will be received and stored in the session.
     * @param token the existing token
     * @throws LogInException if status code is 401 (UNAUTHORIZED)
     * @throws ClientException if all other non-200 status codes
     * @throws javax.ws.rs.ProcessingException if the response is missing/malformed
     */
    public void logIn(String token) {
        if (session.isLoggedIn())
            logOut();

        UserFromTokenRequest request = new UserFromTokenRequest(token);

        // We always use POST when sending secrets
        Response response = session.unauthorizedPath("users/token").post(Entity.json(request));
        if (!ResponseCode.successfulPostWithEntity(response.getStatusInfo()))
            throw new ClientException("Failed to get User from token", response);

        UserResponse user = response.readEntity(UserResponse.class);

        session.setLoggedIn(token, user);
    }

    /**
     * Tells the server to invalidate the token and sets
     * the token and logged in user to null.
     * @throws IllegalStateException if client not logged in
     * @throws ClientException if request fails
     */
    public void logOut() {
        if (!session.isLoggedIn())
            throw new IllegalStateException("The session is not logged in");

        Response response = session.authorizedPath("logout").post(Entity.text(""));

        session.setLoggedOut();

        if (!ResponseCode.successfulPost(response.getStatusInfo()))
            throw new ClientException("Failed to log out", response);
    }
}
