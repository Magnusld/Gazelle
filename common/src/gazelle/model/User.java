package gazelle.model;

import javax.persistence.*;
import java.util.HashSet;
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

    //TODO: Passe på at vi ikke lekker denne i alle mulige JSON-responser
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<CourseRole> roles = new HashSet<>();

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
}

