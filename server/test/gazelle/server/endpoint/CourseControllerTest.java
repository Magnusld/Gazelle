package gazelle.server.endpoint;

import gazelle.api.CourseResponse;
import gazelle.api.NewCourseRequest;
import gazelle.model.Course;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
import gazelle.server.service.CourseAndUserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseControllerTest {

    @Autowired
    private CourseController courseController;

    @Autowired
    private CourseAndUserService courseAndUserService;

    @Autowired
    private TestHelper testHelper;

    private Course course1;
    private Course course2;
    private Long user1;
    private String token1;

    @BeforeAll
    public void setup() {
        course1 = testHelper.createTestCourseObject();
        course2 = testHelper.createTestCourseObject();
        user1 = testHelper.createTestUser();
        token1 = testHelper.logInUser(user1);
        courseAndUserService.addOwner(user1, course2.getId());
    }

    @Test
    public void findAll() {
        List<CourseResponse> courses = courseController.findAll(null);
        assertTrue(courses.stream().anyMatch(it -> it.getId().equals(course1.getId())));
        assertTrue(courses.stream().anyMatch(it -> it.getId().equals(course2.getId())));
    }

    @Test
    public void findById() {
        assertThrows(CourseNotFoundException.class, () -> {
            courseController.findById(5000L, null);
        });

        CourseResponse response = courseController.findById(course1.getId(), null);
        assertEquals(course1.getId(), response.getId());
        assertNull(response.isOwner());

        response = courseController.findById(course1.getId(), token1);
        assertEquals(course1.getId(), response.getId());
        assertEquals(course1.getName(), response.getName());
        assertEquals(false, response.isOwner());

        response = courseController.findById(course2.getId(), token1);
        assertEquals(course2.getId(), response.getId());
        assertEquals(course2.getName(), response.getName());
        assertEquals(true, response.isOwner());
    }

    @Test
    public void addNewCourse() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);

        NewCourseRequest course = new NewCourseRequest("Testname");
        assertThrows(InvalidTokenException.class, () -> {
            courseController.addNewCourse(course, "Bearer: dummy");
        });
        CourseResponse response = courseController.addNewCourse(course, token);
        assertNotNull(response.getId());
        assertEquals(course.getName(), response.getName());
        assertEquals(true, response.isOwner());
        assertEquals(false, response.isFollower());
        assertNull(response.getCurrentPost());
        assertNull(response.getNextPost());

        //Check that user is an owner
        assertTrue(courseAndUserService.isOwning(user, response.getId()));
        testHelper.deleteTestCourse(response.getId());
        testHelper.deleteTestUser(user);
    }

    @Test
    public void deleteCourse() {
        final Long localUser = testHelper.createTestUser();
        final String localToken = testHelper.logInUser(localUser);

        assertThrows(CourseNotFoundException.class, () -> {
            courseController.deleteCourse(5000L, localToken);
        });

        NewCourseRequest course = new NewCourseRequest("Testname");
        CourseResponse response = courseController.addNewCourse(course, localToken);

        assertEquals(true, response.isOwner());

        assertThrows(MissingAuthorizationException.class, () -> {
            courseController.deleteCourse(response.getId(), null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseController.deleteCourse(response.getId(), "Bearer: dummy");
        });
        assertThrows(AuthorizationException.class, () -> {
            courseController.deleteCourse(response.getId(), token1);
        });
        courseController.deleteCourse(response.getId(), localToken);

        assertThrows(CourseNotFoundException.class, () -> {
            courseAndUserService.isOwning(localUser, response.getId());
        });

        testHelper.deleteTestUser(localUser);
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestCourse(course1);
        testHelper.deleteTestCourse(course2);
        testHelper.deleteTestUser(user1);
    }
}
