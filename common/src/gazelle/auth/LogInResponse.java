package gazelle.auth;

import gazelle.model.User;

import javax.persistence.Entity;

@Entity
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
}
