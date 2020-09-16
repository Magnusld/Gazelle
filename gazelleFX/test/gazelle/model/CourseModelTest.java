package gazelle.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CourseModelTest {

	@Test
    public void testCourseModel() {
        User u1 = new User();
        Course c1 = new Course("TestCource TTC 1000", u1);
        User u2 = new User();
        Course c2 = new Course("TestCource TTC 2000", u2);
        Post p1 = new Post(c1);
        User u3 = new User();
        User u4 = new User();
        List<User> u1Followers = new ArrayList<User>();
        u1Followers.add(u2);
        u1Followers.add(u3);
        u1Followers.add(u4);
        c1.addPost(p1);
        u1.addCourse(c2);
        c1.addFollower(u2);
        c1.addFollower(u3);
        c1.addFollower(u4);
        assertEquals(u1.getCourses().get(0), c1);
        assertEquals(u1.getCourses().get(1), c2);
        assertEquals(c1.getName(), "TestCource TTC 1000");
        assertEquals(c2.getName(), "TestCource TTC 2000");
        assertEquals(c1.getOwners().get(0), u1);
        assertEquals(c1.getPosts().get(0), p1);
        assertEquals(u2.getCourses().get(1), c1);
        assertEquals(c1.getFollowers(), u1Followers);
    }
}