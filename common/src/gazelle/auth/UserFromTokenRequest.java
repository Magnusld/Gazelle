package gazelle.auth;

import javax.persistence.Entity;

@Entity
public class UserFromTokenRequest {
    private String token;

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
