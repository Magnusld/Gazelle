package gazelle.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseUserTest {

    @Test
    public void testCourseOwnership() {
        Database db = new Database();
        Course c1 = db.newCourse("TDT4320");
        User u1 = db.newUser();
        User u2 = db.newUser();

        assertFalse(c1.getOwners().contains(u1));
        assertFalse(u1.getOwnedCourses().contains(c1));

        c1.addOwner(u1);
        assertTrue(c1.getOwners().contains(u1));
        assertTrue(u1.getOwnedCourses().contains(c1));

        u2.addAsOwner(c1);
        assertTrue(c1.getOwners().contains(u2));
        assertTrue(u2.getOwnedCourses().contains(c1));

        u1.removeAsOwner(c1);
        assertFalse(c1.getOwners().contains(u1));
        assertFalse(u1.getOwnedCourses().contains(c1));

        c1.removeOwner(u2);
        assertFalse(c1.getOwners().contains(u2));
        assertFalse(u2.getOwnedCourses().contains(c1));
    }
}