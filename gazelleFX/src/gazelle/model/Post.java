package gazelle.model;

public class Post {

    private Course course;

    public Post(Course course) {
        setCourse(course);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        Course oldCourse = this.course;

        if (oldCourse != null) {
            this.course = null;
            oldCourse.removePost(this);
        }

        this.course = course;
        if (course != null)
            course.addPost(this);
    }
}
