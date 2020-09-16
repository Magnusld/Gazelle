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
        if(this.course != null)
        this.course = course;
    }
}
