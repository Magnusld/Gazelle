package gazelle.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "following")
    private Set<User> followers = new HashSet<>();

    @ManyToMany(mappedBy = "owning")
    private Set<User> owners = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = {CascadeType.ALL})
    private Set<Post> posts = new HashSet<>();

    protected Course() {}

    public Course(String name) {
        this.name = name;
    }

    public void validate() throws ModelException {
        if (name.length() < 4)
            throw new ModelException("Løpsnavn må være minst 4 bokstaver");
        if (!name.trim().equals(name))
            throw new ModelException("Løpsnavn kan ikke starte eller slutte med tomrom");
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<User> followers) {
        this.followers = followers;
    }

    public Set<User> getOwners() {
        return owners;
    }

    public void setOwners(Set<User> owners) {
        this.owners = owners;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @PreRemove
    private void removeGroupsFromUsers() {
        for (User user : followers) {
            user.getFollowing().remove(this);
        }
        for (User user : owners) {
            user.getOwning().remove(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
