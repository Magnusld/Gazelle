package gazelle.auth;


import gazelle.model.ModelException;
import gazelle.model.User;

import java.util.Objects;

public class SignUpRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;

    protected SignUpRequest() {}

    public SignUpRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void buildUser() throws ModelException {
        User user = new User(this.firstname, this.lastname, this.email, this.password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SignUpRequest request = (SignUpRequest) o;
        return Objects.equals(email, request.email)
                && Objects.equals(password, request.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }
}
