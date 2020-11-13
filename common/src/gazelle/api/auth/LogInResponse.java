package gazelle.api.auth;

import gazelle.model.User;

import java.util.Objects;

public class LogInResponse {
    private String token;
    private User user;

    protected LogInResponse() {}

    public LogInResponse(String token, User user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
