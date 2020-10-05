package gazelle.model;

import javax.persistence.*;

/**
 * A relationship between a course and a user with extra info describing the role.
 * Based on: https://www.baeldung.com/jpa-many-to-many#many-to-many-using-a-composite-key
 *
 * <p>The Primary Key is embedded, which in this case means it is based on the foreign keys
 * of the Course and the User. Thus there can only be one role between a Course and a User.
 */

@Entity
public class CourseRole {

    public enum CourseRoleType {
        FOLLOWER,
        OWNER
    }

    @EmbeddedId
    private CourseRoleKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @Enumerated(EnumType.ORDINAL)
    private CourseRoleType roleType;

    protected CourseRole() {}

    public CourseRole(User user, Course course, CourseRoleType roleType) {
        this.user = user;
        this.course = course;
        this.roleType = roleType;
    }

    public CourseRoleKey getId() {
        return id;
    }

    public void setId(CourseRoleKey id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CourseRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(CourseRoleType roleType) {
        this.roleType = roleType;
    }
}