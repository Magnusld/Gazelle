package gazelle.auth;

public class LogOutRequest {
    private String token;

    protected LogOutRequest() {}

    public LogOutRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
