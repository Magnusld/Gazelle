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
    private Long userid;

    @Column(name = "course_id")
    private Long courseid;

    protected CourseRoleKey() {}

    public CourseRoleKey(Long userid, Long courseid) {
        this.userid = userid;
        this.courseid = courseid;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getCourseid() {
        return courseid;
    }

    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseRoleKey that = (CourseRoleKey) o;
        return Objects.equals(userid, that.userid) &&
                Objects.equals(courseid, that.courseid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userid, courseid);
    }
}
