package gazelle.server.endpoint;

import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
import gazelle.server.service.CourseRoleService;
import gazelle.server.service.TokenAuthService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseControllerTest {

    @Autowired
    private LoginEndpoint loginEndpoint;

    @Autowired
    private CourseController courseController;

    @Autowired
    private CourseRoleService courseRoleService;

    @Autowired
    private UserController userController;

    private User user1;
    private String user1Token;
    private User user2;
    private String user2Token;

    @BeforeAll
    public void setup() {
        SignUpRequest request = new SignUpRequest("TEST_USR", "HELLO-SAILOR");
        LogInResponse response = loginEndpoint.signup(request);
        user1 = response.getUser();
        user1Token = TokenAuthService.addBearer(response.getToken());

        request = new SignUpRequest("TEST_USR2", "HELLO-WORLD");
        response = loginEndpoint.signup(request);
        user2 = response.getUser();
        user2Token = TokenAuthService.addBearer(response.getToken());
    }

    private Course addTmpCourse() {
        Course course = new Course("Test");
        return courseController.addNewCourse(course, user1Token);
    }

    private void deleteTmpCourse(Course course) {
        courseController.deleteCourse(course.getId(), user1Token);
    }

    @Test
    public void testFindAll() {
        // Should be empty
        assertFalse(courseController.findAll().iterator().hasNext());

        Course c = addTmpCourse();

        Iterator<Course> it = courseController.findAll().iterator();
        assertEquals(c, it.next());
        assertFalse(it.hasNext());

        deleteTmpCourse(c);
    }

    @Test
    public void testFindOne() {
        Course c = addTmpCourse();

        Long id = c.getId();
        assertEquals(c, courseController.findOne(id));

        deleteTmpCourse(c);

        assertThrows(CourseNotFoundException.class, () -> {
            courseController.findOne(c.getId());
        });
    }

    @Test
    public void testAddNewCourse() {
        Course course = new Course("Test");
        Course course2 = new Course("Test2");

        assertThrows(InvalidTokenException.class, () -> {
            courseController.addNewCourse(course, "dummy-token");
        });
        courseController.addNewCourse(course, user1Token);
        courseController.addNewCourse(course2, user2Token);

        assertEquals(CourseRole.CourseRoleType.OWNER,
                courseRoleService.getCourseRole(user1, course));

        assertNull(courseRoleService.getCourseRole(user2, course));

        courseController.deleteCourse(course.getId(), user1Token);
        courseController.deleteCourse(course2.getId(), user2Token);
    }

    @Test
    public void testDeleteCourse() {
        Course c = addTmpCourse();
        assertThrows(AuthorizationException.class, () -> {
            courseController.deleteCourse(c.getId(), user2Token);
        });
        assertThrows(MissingAuthorizationException.class, () -> {
            courseController.deleteCourse(c.getId(), null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseController.deleteCourse(c.getId(), "dummy-token");
        });
        deleteTmpCourse(c);
        assertThrows(CourseNotFoundException.class, () -> {
            courseController.deleteCourse(c.getId(), user1Token);
        });
    }

    @AfterAll
    public void cleanup() {
        userController.deleteUser(user1.getId(), user1Token);
        userController.deleteUser(user2.getId(), user2Token);
    }
}
