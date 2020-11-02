package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.server.TestHelper;
import gazelle.server.error.*;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.CourseAndUserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseControllerTest {

    @Autowired
    private CourseController courseController;

    @Autowired
    private UserController userController;

    @Autowired
    private CourseAndUserService courseAndUserService;

    @Autowired
    private TestHelper testHelper;

    private Course course1;
    private Course course2;

    @BeforeAll
    public void setup() {
        course1 = testHelper.createTestCourseObject();
        course2 = testHelper.createTestCourseObject();
    }

    @Test
    public void findAll() {
        List<Course> courses = new ArrayList<>();
        courseController.findAll().forEach(courses::add);
        assertEquals(2, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
    }

    @Test
    public void findById() {
        assertEquals(course1, courseController.findById(course1.getId()));
        assertThrows(CourseNotFoundException.class, () -> {
            courseController.findById(5000L);
        });
    }

    @Test
    public void addNewCourse() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);
        assertThrows(ExistingEntityException.class, () -> {
            courseController.addNewCourse(course1, token);
        });

        Course course = new Course("Testname");
        assertThrows(InvalidTokenException.class, () -> {
            courseController.addNewCourse(course, "Bearer: dummy");
        });
        courseController.addNewCourse(course, token);
        assertNotNull(course.getId());

        //Check that user is an owner
        assertTrue(courseAndUserService.isOwning(user, course.getId()));
        testHelper.deleteTestCourse(course);
        testHelper.deleteTestUser(user);
    }

    @Test
    public void deleteCourse() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);
        Long user2 = testHelper.createTestUser();
        String token2 = testHelper.logInUser(user2);

        assertThrows(CourseNotFoundException.class, () -> {
            courseController.deleteCourse(5000L, token);
        });

        Course course = new Course("Testname");
        courseController.addNewCourse(course, token);

        assertThrows(MissingAuthorizationException.class, () -> {
            courseController.deleteCourse(course.getId(), null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseController.deleteCourse(course.getId(), "Bearer: dummy");
        });
        assertThrows(AuthorizationException.class, () -> {
            courseController.deleteCourse(course.getId(), token2);
        });
        courseController.deleteCourse(course.getId(), token);

        assertThrows(CourseNotFoundException.class, () -> {
            courseAndUserService.isOwning(user, course.getId());
        });

        testHelper.deleteTestUser(user);
        testHelper.deleteTestUser(user2);
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestCourse(course1);
        testHelper.deleteTestCourse(course2);
    }
}
