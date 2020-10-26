package gazelle.auth;


import java.util.Objects;

public class SignUpRequest {
    private String username;
    private String password;

    protected SignUpRequest() {}

    public SignUpRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpRequest request = (SignUpRequest) o;
        return Objects.equals(username, request.username) &&
                Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
