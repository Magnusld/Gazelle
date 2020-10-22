package gazelle.server;


import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.User;
import gazelle.server.endpoint.CourseController;
import gazelle.server.endpoint.CourseRoleController;
import gazelle.server.error.*;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.CourseRoleRepository;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
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
public class CourseTest {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    CourseRoleRepository courseRoleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseController courseController;

    @Autowired
    CourseRoleController courseRoleController;

    @Autowired
    TokenAuthService tokenAuthService;

    User user1;
    String user1Token;
    User user2;
    String user2Token;

    @BeforeAll
    public void setupTests() {
        user1 = userRepository.save(new User("tester", "tester"));
        user1Token = TokenAuthService.addBearer(tokenAuthService.createTokenForUser(user1));
        user2 = userRepository.save(new User("test", "test"));
        user2Token = TokenAuthService.addBearer(tokenAuthService.createTokenForUser(user2));
    }

    @Test
    public void courseRepositoryTest() {
        Course course = courseRepository.save(new Course("test1"));
        assertTrue(courseRepository.findById(course.getId()).isPresent());
        courseRepository.delete(courseRepository.findById(course.getId()).get());
        assertFalse(courseRepository.findById(course.getId()).isPresent());
    }

    @Test
    public void courseControllerTest() {
        Course course = courseController.addNewCourse(new Course("test2"), user1Token);
        assertTrue(courseRepository.findById(course.getId()).isPresent());
        assertNotNull(courseController.findOne(course.getId()));
        Throwable exception = assertThrows(ExistingEntityException.class,
                () -> courseController.addNewCourse(course, user1Token));
        Throwable exception1 = assertThrows(CourseNotFoundException.class,
                () -> courseController.findOne(42L));
        courseController.deleteCourse(course.getId(), user1Token);
    }

    @Test
    public void courseRoleControllerTest() {
        Course course = courseController.addNewCourse(new Course("test3"), user1Token);
        courseRoleController.setCourseRole(user1.getId(),
                course.getId(),
                CourseRole.CourseRoleType.OWNER,
                user1Token);
        assertThrows(AuthorizationException.class, () -> {
            courseRoleController.setCourseRole(user1.getId(),
                    course.getId(),
                    CourseRole.CourseRoleType.OWNER,
                    user2Token);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseRoleController.setCourseRole(user1.getId(),
                    course.getId(),
                    CourseRole.CourseRoleType.OWNER,
                    "Bearer summy-doken");
        });

        assertTrue(courseRoleRepository.findByUserAndCourse(user1, course).isPresent());
        Throwable exception1 = assertThrows(CourseNotFoundException.class,
                () -> courseRoleController.setCourseRole(user1.getId(),
                        12L,
                        CourseRole.CourseRoleType.OWNER, user1Token));
        assertTrue(
                courseRoleController.findCoursesForUser(user2.getId(), null, user2Token).isEmpty());

        List<Course> user1Courses =
                courseRoleController.findCoursesForUser(user1.getId(), CourseRole.CourseRoleType.OWNER, user1Token);
        assertEquals(1, user1Courses.size());
        assertEquals(course.getName(), user1Courses.get(0).getName());
        assertEquals(course.getId(), user1Courses.get(0).getId());

        assertThrows(AuthorizationException.class,
                () -> courseRoleController.findCoursesForUser(5000L, null, user1Token));

        assertThrows(InvalidTokenException.class,
                () -> courseRoleController.findCoursesForUser(5001L, null, "Bearer dummy-token"));

        courseController.deleteCourse(course.getId(), user1Token);
    }

    @AfterAll
    public void cleanup() {
        //TODO: Use UserController
        userRepository.deleteById(user1.getId());
        userRepository.deleteById(user2.getId());
    }
}
