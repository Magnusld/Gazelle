package gazelle.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseModelTest {

    @Test
    public void testCourseGettersAndSetters() {
        User owner = new User();
        Course c1 = new Course("test", owner);
        assertTrue(c1.getOwners().size() == 1);
        assertTrue(c1.getOwners().contains(owner));
        assertTrue(owner.getOwnedCourses().contains(c1));
        assertTrue(c1.getPosts().isEmpty());
        assertTrue(c1.getFollowers().isEmpty());

        assertEquals("test", c1.getName());

        c1.setName("test2");
        assertEquals("test2", c1.getName());
    }

	@Test
    public void testCourseAddUser() {
        Course c1 = new Course("TestCourse TTC 1000");
        User u1 = new User();

        c1.addFollower(u1);
        assertTrue(c1.getFollowers().contains(u1));
        assertTrue(u1.getFollowedCourses().contains(c1));

        c1.addOwner(u1);
        assertTrue(c1.getOwners().contains(u1));
        assertTrue(u1.getOwnedCourses().contains(c1));
        assertFalse(c1.getFollowers().contains(u1));
        assertFalse(u1.getFollowedCourses().contains(c1));

        assertFalse(u1.removeFollowedCourse(c1));
        assertTrue(u1.removeOwnedCourse(c1));

        assertTrue(c1.getFollowers().isEmpty() && c1.getOwners().isEmpty());
        assertTrue(u1.getFollowedCourses().isEmpty() && u1.getOwnedCourses().isEmpty());

        c1.addOwner(u1);
        assertTrue(c1.getOwners().contains(u1));
        assertTrue(u1.getOwnedCourses().contains(c1));
        assertFalse(c1.getFollowers().contains(u1));
        assertFalse(u1.getFollowedCourses().contains(c1));

        c1.addFollower(u1);
        assertFalse(c1.getOwners().contains(u1));
        assertFalse(u1.getOwnedCourses().contains(c1));
        assertTrue(c1.getFollowers().contains(u1));
        assertTrue(u1.getFollowedCourses().contains(c1));
    }

    @Test
    public void testCourseRemoveUser() {
        Course c1 = new Course("TestCourse TTC 1000");
        User u1 = new User();
        User u2 = new User();

        c1.addFollower(u1);
        u2.addFollowedCourse(c1);
        assertTrue(c1.getFollowers().contains(u1) && u1.getFollowedCourses().contains(c1));

        c1.removeFollower(u1);
        u2.removeOwnedCourse(c1); // NOP
        c1.removeOwner(u2); // NOP
        assertTrue(c1.getFollowers().contains(u2) && u2.getFollowedCourses().contains(c1));
        assertFalse(c1.getFollowers().contains(u1) || u1.getFollowedCourses().contains(c1));
        u2.removeFollowedCourse(c1);
        assertFalse(c1.getFollowers().contains(u2) || u2.getFollowedCourses().contains(c1));

        //Same but with owner <-> follower inverted

        c1.addOwner(u1);
        u2.addOwnedCourse(c1);
        assertTrue(c1.getOwners().contains(u1) && u1.getOwnedCourses().contains(c1));

        c1.removeOwner(u1);
        u2.removeFollowedCourse(c1); // NOP
        c1.removeFollower(u2); // NOP
        assertTrue(c1.getOwners().contains(u2) && u2.getOwnedCourses().contains(c1));
        assertFalse(c1.getFollowers().contains(u1) || u1.getFollowedCourses().contains(c1));
        u2.removeFollowedCourse(c1);
        assertFalse(c1.getFollowers().contains(u2) || u2.getFollowedCourses().contains(c1));
    }

    @Test
    public void testCourseAddRemovePost() {
        Course c1 = new Course("TestCourse TTC 1000");
        Course c2 = new Course("TestCourse TTC 2000");
        Post p1 = new Post(c1);
        assertTrue(c1.getPosts().contains(p1));
        assertEquals(c1, p1.getCourse());

        c2.addPost(p1);
        assertFalse(c1.getPosts().contains(p1));
        assertTrue(c2.getPosts().contains(p1));
        assertEquals(c2, p1.getCourse());
        c2.addPost(p1); // NOP
        assertFalse(c1.getPosts().contains(p1));
        assertTrue(c2.getPosts().contains(p1));
        assertEquals(c2, p1.getCourse());

        c2.removePost(p1);
        assertFalse(c2.getPosts().contains(p1));
        assertEquals(null, p1.getCourse());
    }
}