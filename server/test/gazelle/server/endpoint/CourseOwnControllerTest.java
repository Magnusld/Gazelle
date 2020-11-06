package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.GazelleException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
import gazelle.server.service.CourseAndUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseOwnControllerTest {

    private Course course1;
    private Course course2;
    private User user1;
    private User user2;
    private String token1;
    private String token2;

    @Autowired
    private CourseOwnController courseOwnController;

    @Autowired
    private CourseAndUserService courseAndUserService;
    @Autowired
    private TestHelper testHelper;

    @BeforeAll
    public void setup() {
        user1 = testHelper.createTestUserObject();
        user2 = testHelper.createTestUserObject();
        token1 = testHelper.logInUser(user1.getId());
        token2 = testHelper.logInUser(user2.getId());
        course1 = testHelper.createTestCourseObject();
        course2 = testHelper.createTestCourseObject();
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

    @Test
    public void addCourseOwner() {
        courseAndUserService.addOwner(user1.getId(), course1.getId());
        assertThrows(MissingAuthorizationException.class, () -> {
            courseOwnController.addCourseOwner(user2.getId(), course1.getId(), null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseOwnController.addCourseOwner(user2.getId(), course1.getId(), "Unbearer: 123");
        });
        assertThrows(AuthorizationException.class, () -> {
            courseOwnController.addCourseOwner(user2.getId(), course1.getId(), token2);
        });
        courseOwnController.addCourseOwner(user2.getId(), course1.getId(), token1);
        assertEquals(2, courseOwnController.getCourseOwners(course1.getId()));
        assertTrue(courseOwnController.getCourseOwners(course1.getId()).contains(user2));
    }

    @Test
    public void removeCourseOwner() {
        courseAndUserService.addOwner(user1.getId(), course1.getId());
        courseAndUserService.addOwner(user2.getId(), course1.getId());
        User user3 = testHelper.createTestUserObject();
        String token3 = testHelper.logInUser(user3.getId());
        assertThrows(MissingAuthorizationException.class, () -> {
            courseOwnController.removeCourseOwner(user1.getId(), course1.getId(), null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseOwnController.removeCourseOwner(user1.getId(), course1.getId(), "Unbearer: 123");
        });
        assertThrows(AuthorizationException.class, () -> {
            courseOwnController.removeCourseOwner(user1.getId(), course1.getId(), token3);
        });
        courseOwnController.removeCourseOwner(user2.getId(), course1.getId(), token2);
        // No har kurset kun éin eigar
        assertThrows(GazelleException.class, () -> {
           courseOwnController.removeCourseOwner(user1.getId(), course1.getId(), token1);
            // Dette vil vere den einaste eigaren
        });
    }

}
