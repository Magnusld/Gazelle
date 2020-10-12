package gazelle.server;


import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.User;
import gazelle.server.endpoint.CourseController;
import gazelle.server.endpoint.CourseRoleController;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.ExistingEntityException;
import gazelle.server.error.GazelleException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.CourseRoleRepository;
import gazelle.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

    User user;
    User user1;

    @BeforeAll
    public void setupTests() {
        user = userRepository.save(new User("tester", "tester"));
        user1 = userRepository.save(new User("test", "test"));
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
        Course course = courseController.addNewCourse(new Course("test2"));
        assertTrue(courseRepository.findById(course.getId()).isPresent());
        assertNotNull(courseController.findOne(course.getId()));
        Throwable exception = assertThrows(ExistingEntityException.class,
                () -> courseController.addNewCourse(course));
        Throwable exception1 = assertThrows(CourseNotFoundException.class,
                () -> courseController.findOne(42l));
    }

    @Test
    public void courseRoleControllerTest() {
        Course course = courseController.addNewCourse(new Course("test3"));
        courseRoleController.setCourseRole(user.getId(),
                course.getId(),
                CourseRole.CourseRoleType.OWNER);
        assertTrue(courseRoleRepository.findByUserAndCourse(user, course).isPresent());
        Throwable exception = assertThrows(UserNotFoundException.class,
                () -> courseRoleController.setCourseRole(52l, course.getId(),
                        CourseRole.CourseRoleType.OWNER));
        Throwable exception1 = assertThrows(CourseNotFoundException.class,
                () -> courseRoleController.setCourseRole(user.getId(),
                        12l,
                        CourseRole.CourseRoleType.OWNER));
        assertEquals(CourseRole.CourseRoleType.OWNER,
                courseRoleController.getCourseRole(user.getId(),course.getId()).getRoleType());
        GazelleException exception2 = assertThrows(GazelleException.class,
                () -> courseRoleController.getCourseRole(32l,52l));
        assertEquals(exception2.getReason(), "Course role not found");
        assertTrue(courseRoleController.findCoursesForUser(user1.getId(), null).isEmpty());
        assertEquals(course.getName(), courseRoleController.findCoursesForUser(user.getId(),
                CourseRole.CourseRoleType.OWNER).get(0).getName());
        assertEquals(course.getId(), courseRoleController.findCoursesForUser(user.getId(),
                CourseRole.CourseRoleType.OWNER).get(0).getId());
        Throwable exception3 = assertThrows(UserNotFoundException.class,
                () -> courseRoleController.findCoursesForUser(53l,null));

    }

}
