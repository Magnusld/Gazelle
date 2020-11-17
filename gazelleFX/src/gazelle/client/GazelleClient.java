package gazelle.client;

public class GazelleClient {
    private final GazelleSession session;
    private final LoginClient login;

    public GazelleClient(String baseURL) {
        session = new GazelleSession(baseURL);
        login = new LoginClient(session);
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
}
