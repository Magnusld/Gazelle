package gazelle.api;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

public class UserResponse {
    private Long id;
    private String firstName;
    private String lastName;

    public static class Builder {
        private final UserResponse built;

        public Builder() {
            built = new UserResponse();
        }

        public UserResponse build() {
            built.validate();
            return built;
        }

        public Builder id(Long id) {
            built.setId(id);
            return this;
        }

        public Builder firstName(String firstName) {
            built.setFirstName(firstName);
            return this;
        }

        public Builder lastName(String lastName) {
            built.setLastName(lastName);
            return this;
        }
    }

    protected UserResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(firstName);
        Objects.requireNonNull(lastName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserResponse)) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(firstName, that.firstName)
                && Objects.equals(lastName, that.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
