package gazelle.server.endpoint;

import gazelle.api.CourseResponse;
import gazelle.api.UserResponse;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.GazelleException;
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
public class CourseOwnControllerTest {

    private Long course1;
    private Long course2;
    private Long user1;
    private Long user2;
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
        user1 = testHelper.createTestUser();
        user2 = testHelper.createTestUser();
        token1 = testHelper.logInUser(user1);
        token2 = testHelper.logInUser(user2);
        course1 = testHelper.createTestCourse();
        course2 = testHelper.createTestCourse();
    }

    @Test
    public void getOwnedCourses() {
        courseAndUserService.addOwner(user1, course1);
        courseAndUserService.addOwner(user1, course2);
        List<CourseResponse> courses = courseOwnController.getOwnedCourses(user1);
        assertEquals(2, courses.size());
        assertTrue(courses.stream().anyMatch(it -> it.getId().equals(course1)));
        assertTrue(courses.stream().anyMatch(it -> it.getId().equals(course2)));
        courseAndUserService.removeOwner(user1, course1);
        courseAndUserService.removeOwner(user1, course2);
    }

    @Test
    public void getCourseOwners() {
        final Long user3 = testHelper.createTestUser();
        courseAndUserService.addOwner(user1, course1);
        courseAndUserService.addOwner(user2, course1);
        assertTrue(courseOwnController.getCourseOwners(course2).isEmpty());
        List<UserResponse> owners = courseOwnController.getCourseOwners(course1);
        assertEquals(2, owners.size());
        assertTrue(owners.stream().anyMatch(it -> it.getId().equals(user1)));
        assertTrue(owners.stream().anyMatch(it -> it.getId().equals(user2)));
        assertFalse(owners.stream().anyMatch(it -> it.getId().equals(user3)));
        courseAndUserService.removeOwner(user1, course1);
        courseAndUserService.removeOwner(user2, course1);
        testHelper.deleteTestUser(user3);
    }

    @Test
    public void addCourseOwner() {
        courseAndUserService.addOwner(user1, course1);
        assertThrows(MissingAuthorizationException.class, () -> {
            courseOwnController.addCourseOwner(user2, course1, null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseOwnController.addCourseOwner(user2, course1, "Unbearer: 123");
        });
        assertThrows(AuthorizationException.class, () -> {
            courseOwnController.addCourseOwner(user2, course1, token2);
        });
        courseOwnController.addCourseOwner(user2, course1, token1);
        List<UserResponse> owners = courseOwnController.getCourseOwners(course1);
        assertEquals(2, owners.size());
        assertTrue(owners.stream().anyMatch(it -> it.getId().equals(user2)));
        courseAndUserService.removeOwner(user1, course1);
        courseAndUserService.removeOwner(user2, course1);
    }

    @Test
    public void removeCourseOwner() {
        courseAndUserService.addOwner(user1, course1);
        courseAndUserService.addOwner(user2, course1);
        Long user3 = testHelper.createTestUser();
        String token3 = testHelper.logInUser(user3);
        assertThrows(MissingAuthorizationException.class, () -> {
            courseOwnController.removeCourseOwner(user1, course1, null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            courseOwnController.removeCourseOwner(user1, course1, "Unbearer: 123");
        });
        assertThrows(AuthorizationException.class, () -> {
            courseOwnController.removeCourseOwner(user1, course1, token3);
        });
        courseOwnController.removeCourseOwner(user2, course1, token2);
        // No har kurset kun éin eigar
        assertThrows(GazelleException.class, () -> {
            courseOwnController.removeCourseOwner(user1, course1, token1);
            // Dette vil vere den einaste eigaren
        });
        courseAndUserService.removeOwner(user1, course1);
        testHelper.deleteTestUser(user3);
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestUser(user1);
        testHelper.deleteTestUser(user2);
        testHelper.deleteTestCourse(course1);
        testHelper.deleteTestCourse(course2);
    }
}
