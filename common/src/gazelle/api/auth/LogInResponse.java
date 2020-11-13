package gazelle.api.auth;

import gazelle.api.UserResponse;

import java.util.Objects;

public class LogInResponse {
    private String token;
    private UserResponse user;

    protected LogInResponse() {}

    public LogInResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogInResponse that = (LogInResponse) o;
        return Objects.equals(token, that.token)
                && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, user);
    }
}
