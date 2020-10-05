package gazelle.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

/**
 * The primary key for the role relationship between Users and Courses,
 * basing our key on their foreign keys.
 * Can not contain entities, must be serializable and have equals and hashCode defined.
 * Based on: https://www.baeldung.com/jpa-many-to-many#3-using-a-composite-key-in-jpa
 */
@Embeddable
public class CourseRoleKey implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "course_id")
    private Long courseId;

    protected CourseRoleKey() {}

    public CourseRoleKey(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseRoleKey that = (CourseRoleKey) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(courseId, that.courseId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, courseId);
    }
}
