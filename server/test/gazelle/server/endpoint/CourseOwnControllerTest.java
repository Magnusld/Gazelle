package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.service.CourseAndUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class CourseOwnControllerTest {

    @Autowired
    private CourseOwnController courseOwnController;

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private CourseAndUserService courseAndUserService;

    private Course course1;
    private Course course2;
    private User user1;
    private User user2

    @BeforeAll
    public void setup() {
        course1 = testHelper.createTestCourseObject();
        course2 = testHelper.createTestCourseObject();
        user1 = testHelper.createTestUserObject();
        user2 = testHelper.createTestUserObject();
    }

    @Test
    public void getOwnedCourses() {
        courseAndUserService.addOwner(user1, course1);
        courseAndUserService.addOwner(user1, course2);
        ArrayList<Course> courses;
        courses = courseOwnController.getOwnedCourses(user1.getId());
        assertEquals(2, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
    }

    @Test
    public void getCourseOwners() {
        User user3 = testHelper.createTestUserObject();
        courseAndUserService.addOwner(user1, course1);
        courseAndUserService.addOwner(user2, course1);
        ArrayList<User> owners;
        owners = courseOwnController.getCourseOwners(course1.getId());
        assertEquals(2, owners.size());
        assertTrue(owners.contains(user1));
        assertTrue(owners.contains(user2));
        assertFalse(owners.contains(user3));
    }
}
