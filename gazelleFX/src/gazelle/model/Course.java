package gazelle.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Course extends DatabaseRow {
    private String name;

    public Course(Database.Id id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addOwner(User owner) {
        getDatabase().addOwnerToCourse(owner, this);
    }

    public boolean removeOwner(User owner) {
        return getDatabase().removeOwnerOfCourse(owner, this);
    }

    public List<User> getOwners() {
        return getDatabase().getCourseOwners(this);
    }
}
