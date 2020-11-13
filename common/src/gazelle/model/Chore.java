package gazelle.model;

import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Chore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * The key used to order the items on the post
     */
    @Column(nullable = false)
    private Long key;

    @Column(nullable = false)
    private String text;

    @Column(nullable = true)
    private Date dueDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Post post;

    protected Chore() {}

    public Chore(Long key, String text, @Nullable Date dueDate, Post post) {
        this.key = key;
        this.text = text;
        this.dueDate = dueDate;
        this.post = post;
    }

    public @Nullable Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public @Nullable Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(@Nullable Date dueDate) {
        this.dueDate = dueDate;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chore)) return false;
        Chore chore = (Chore) o;
        return Objects.equals(id, chore.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
