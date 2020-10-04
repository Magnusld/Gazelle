package gazelle.auth;

public class UserFromTokenRequest {
    private String token;

    protected UserFromTokenRequest() {}

    public UserFromTokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
