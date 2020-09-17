package gazelle.model;

import java.util.List;

public class User extends DatabaseRow {

    public User(Database.Id id) {
        super(id);
    }

    public void addAsOwner(Course course) {
        getDatabase().addOwnerToCourse(this, course);
    }

    public boolean removeAsOwner(Course course) {
        return getDatabase().removeOwnerOfCourse(this, course);
    }

    public List<Course> getOwnedCourses() {
        return getDatabase().getCoursesOwned(this);
    }
}
