package gazelle.model;

public class Post {

    private Course course;

    public Post(Course course){
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        if(this.course == course)
            return;
        if(this.course != null) {
            Course oldCourse = this.course;
            this.course = null;
            oldCourse.removePost(this);
        }
        this.course = course;
        course.addPost(this);
    }
}
