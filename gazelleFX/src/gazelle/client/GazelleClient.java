package gazelle.client;

import gazelle.api.UserResponse;

public class GazelleClient {
    private final GazelleSession session;
    private final LoginClient login;
    private final CourseClient courses;
    private final PostClient posts;

    public GazelleClient(String baseURL) {
        session = new GazelleSession(baseURL);
        login = new LoginClient(session);
        courses = new CourseClient(session);
        posts = new PostClient(session);
    }

    /**
     * Sets the baseURL for all other requests
     * @param baseURL the server URL, including protocol
     */
    public void setBaseURL(String baseURL) {
        session.setBaseURL(baseURL);
    }

    public GazelleSession session() {
        return session;
    }

    public LoginClient login() {
        return login;
    }

    public CourseClient courses() {
        return courses;
    }

    public PostClient posts() {
        return posts;
    }

    /**
     * Henter id-en til den påloggede brukeren
     * @return Long id
     * @throws IllegalStateException hvis ingen er logget inn
     */
    public Long loggedInUserId() {
        UserResponse loggedIn = session.getLoggedInUser();
        if (loggedIn == null)
            throw new IllegalStateException("Ikke logget inn");
        return loggedIn.getId();
    }
}
