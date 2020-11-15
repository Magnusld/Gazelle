package gazelle.api;

import gazelle.model.Course;

import java.util.Objects;

public class NewCourseRequest {
    private String name;

    protected NewCourseRequest() {}

    public NewCourseRequest(String name) {
        this.name = name;
    }

    public Course buildCourse() {
        return new Course(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewCourseRequest)) return false;
        NewCourseRequest that = (NewCourseRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}