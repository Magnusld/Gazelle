package gazelle.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private List<Course> courses = new ArrayList<>();

    public User(){
    }

    public List<Course> getCourses(){
        return courses;
    }

    public void addCourse(Course course){
        if(this.courses.contains(course))
            return;
        this.courses.add(course);
        course.addOwner(this);
    }

    public void removeCourse(Course course){
        if(this.courses.remove(course))
            course.removeOwner(this);
    }
}
