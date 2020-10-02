package gazelle.auth;

import javax.persistence.Entity;

@Entity
public class LogOutRequest {
    private String token;

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
