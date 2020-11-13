package gazelle.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class TokenLogIn {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String token;

    protected TokenLogIn() {}

    public TokenLogIn(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public @Nullable Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TokenLogIn)) return false;
        TokenLogIn that = (TokenLogIn) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
