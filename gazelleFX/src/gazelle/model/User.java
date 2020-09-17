package gazelle.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class User {

    private List<Course> ownedCourses = new ArrayList<>();
    private List<Course> followedCourses = new ArrayList<>();

    public User(){
    }

    public List<Course> getOwnedCourses() {
        return Collections.unmodifiableList(ownedCourses);
    }

    public void addOwnedCourse(Course course) {
        removeFollowedCourse(course);
        if (this.ownedCourses.contains(course))
            return;
        this.ownedCourses.add(course);
        course.addOwner(this);
    }

    public boolean removeOwnedCourse(Course course) {
        if (this.ownedCourses.remove(course)) {
            course.removeOwner(this);
            return true;
        }
        return false;
    }

    public List<Course> getFollowedCourses() {
        return Collections.unmodifiableList(followedCourses);
    }

    public void addFollowedCourse(Course course) {
        removeOwnedCourse(course);
        if (this.followedCourses.contains(course))
            return;
        this.followedCourses.add(course);
        course.addFollower(this);
    }

    public boolean removeFollowedCourse(Course course) {
        if (this.followedCourses.remove(course)) {
            course.removeFollower(this);
            return true;
        }
        return false;
    }
}
