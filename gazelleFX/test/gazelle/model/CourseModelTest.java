package gazelle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseModelTest {

	@Test
    public void testCourseAddUser() {
        Course c1 = new Course("TestCource TTC 1000");
        Course c2 = new Course("TestCource TTC 2000");
        Course c3 = new Course("TestCource TTC 3000");

        User u1 = new User();
        User u2 = new User();
        User u3 = new User();

        assertTrue(c1.getFollowers().isEmpty() && c1.getOwners().isEmpty());
        assertTrue(c2.getFollowers().isEmpty() && c2.getOwners().isEmpty());

        c1.addFollower(u1);
        assertTrue(c1.getFollowers().contains(u1));
        assertTrue(u1.getFollowedCourses().contains(c1));

        c1.addOwner(u1);
        assertFalse(c1.getFollowers().contains(u1));
        assertFalse(u1.getFollowedCourses().contains(c1));

        assertFalse(u1.removeFollowedCourse(c1));
        assertTrue(u1.removeOwnedCourse(c1));

        assertTrue(u1.getFollowedCourses().isEmpty());
        assertTrue(c1.getFollowers().isEmpty());
        assertTrue(u1.getOwnedCourses().isEmpty());
        assertTrue(c1.getOwners().isEmpty());
    }
}