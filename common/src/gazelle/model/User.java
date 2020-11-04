package gazelle.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * The Entity representing users of the service.
 * Based on: https://www.baeldung.com/jpa-entities
 * and https://www.baeldung.com/jpa-many-to-many
 */
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String firstname;

    @Column(nullable = false, unique = true)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_course_follow",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnore
    private Set<Course> following = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_course_own",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @JsonIgnore
    private Set<Course> owning = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @JsonIgnore
    private TokenLogIn token;
    
    protected User() {}

    public User(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Course> getFollowing() {
        return following;
    }

    public Set<Course> getOwning() {
        return owning;
    }

    public TokenLogIn getToken() {
        return token;
    }

    public void setToken(TokenLogIn token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    public void verify() throws ModelException {
        if (this.password.length() < 4) {
            throw new ModelException("Passordet må være over 4 tegn.");
        }
        if (this.email.contains(" ")) {
            throw new ModelException("E-post adressen kan ikke inneholde mellomrom.");
        }
        if (this.firstname.contains(" ") || this.lastname.contains(" ")) {
            throw new ModelException("Navn og etternavn kan ikke inneholde mellomrom.");
        }
        if (this.firstname.length() < 1 || this.lastname.length() < 1) {
            throw new ModelException("Ver vennlig å fylle inn både fornavn og etternavn.");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
