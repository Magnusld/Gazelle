package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
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
    private User user2;
    private String token1;
    private String token2;

    @BeforeAll
    public void setup() {
        course1 = testHelper.createTestCourseObject();
        course2 = testHelper.createTestCourseObject();
        user1 = testHelper.createTestUserObject();
        user2 = testHelper.createTestUserObject();
        token1 = testHelper.logInUser(user1.getId());
        token2 = testHelper.logInUser(user2.getId());
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

    /**
     * Støtt på eit problem med denne metoden sidan eg må ta inn ein authorization token
     * Korleis?
     */
    @Test
    public void addCourseOwner() {
        courseAndUserService.addOwner(user1, course1);
        assertThrows(MissingAuthorizationException.class, () -> {
            courseOwnController.addCourseOwner(user2.getId(), course1.getId(), null);
        });
        // Spørsmålet er no, kva er ein invalid token?
        assertThrows(InvalidTokenException.class, )
        assertThrows(AuthorizationException.class, () -> {
            courseOwnController.addCourseOwner(user2.getId(), course1.getId(), token2);
        });

        courseOwnController.addCourseOwner(user2.getId(), course1.getId(), token1); // Antek at det er tokenen til den som skal ha authorization som må vere her?

    }

}
