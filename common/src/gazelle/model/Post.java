package gazelle.model;

import gazelle.util.DateHelper;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @OneToMany(mappedBy = "post", cascade = {CascadeType.ALL})
    private Set<Chore> chores = new HashSet<>();

    protected Post() {}

    public Post(String title, String description, Course course, Date startDate, Date endDate) {
        this.title = title;
        this.description = description;
        this.course = course;
        this.startDate = DateHelper.clone(startDate);
        this.endDate = DateHelper.clone(endDate);
    }

    public @Nullable Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return DateHelper.clone(startDate);
    }

    public void setStartDate(Date startDate) {
        this.startDate = DateHelper.clone(startDate);
    }

    public Date getEndDate() {
        return DateHelper.clone(endDate);
    }

    public void setEndDate(Date endDate) {
        this.endDate = DateHelper.clone(endDate);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Set<Chore> getChores() {
        return chores;
    }

    public void setChores(Set<Chore> chores) {
        this.chores = chores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
