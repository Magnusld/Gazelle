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
    private String username;

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

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
