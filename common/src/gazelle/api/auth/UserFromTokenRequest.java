package gazelle.api.auth;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFromTokenRequest that = (UserFromTokenRequest) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
