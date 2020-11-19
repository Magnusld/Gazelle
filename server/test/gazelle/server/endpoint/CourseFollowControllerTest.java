package gazelle.server.endpoint;

import gazelle.api.CourseResponse;
import gazelle.api.UserResponse;
import gazelle.api.ValueWrapper;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
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
public class CourseFollowControllerTest {

    private Long user1;
    private Long user2;
    private String token1;
    private String token2;
    private Long course1;
    private Long course2;

    @Autowired
    private CourseAndUserService courseAndUserService;

    @Autowired
    private CourseFollowController courseFollowController;

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

        courseAndUserService.addFollower(user1, course1);
        courseAndUserService.addFollower(user1, course2);
        courseAndUserService.addFollower(user2, course2);
        courseAndUserService.addOwner(user2, course1);
        courseAndUserService.addOwner(user2, course2);
    }

    @Test
    public void getFollowedCourses() {
        assertThrows(AuthorizationException.class, () -> {
            courseFollowController.getFollowedCourses(user1, token2);
        });
        List<CourseResponse> followed = courseFollowController.getFollowedCourses(user1, token1);
        assertTrue(followed.stream().anyMatch(course -> course.getId().equals(course1)));
        assertTrue(followed.stream().anyMatch(course -> course.getId().equals(course2)));
        assertEquals(2, followed.size());
    }

    @Test
    public void getCourseFollowers() {
        assertThrows(AuthorizationException.class, () -> {
            courseFollowController.getCourseFollowers(course1, token1);
        });
        List<UserResponse> followers = courseFollowController.getCourseFollowers(course1, token2);
        assertTrue(followers.stream().anyMatch(user -> user.getId().equals(user1)));
        assertTrue(courseAndUserService.isFollowing(user1, course1));
        assertFalse(courseAndUserService.isFollowing(user2, course1));
        assertEquals(1, followers.size());
    }

    @Test
    public void addRemoveCourseFollower() {
        assertThrows(AuthorizationException.class, () -> {
            courseFollowController.addCourseFollower(user2, new ValueWrapper<>(course1), token1);
        });
        assertFalse(courseFollowController.getCourseFollowers(course1, token2)
                .stream().anyMatch(user -> user.getId().equals(user2)));
        courseFollowController.addCourseFollower(user2, new ValueWrapper<>(course1), token2);
        assertTrue(courseFollowController.getCourseFollowers(course1, token2)
                .stream().anyMatch(user -> user.getId().equals(user2)));
        courseFollowController.addCourseFollower(user2, new ValueWrapper<>(course1), token2); //NOP
        assertTrue(courseFollowController.getCourseFollowers(course1, token2)
                .stream().anyMatch(user -> user.getId().equals(user2)));

        assertThrows(AuthorizationException.class, () -> {
            courseFollowController.removeCourseFollower(user2, course1, token1);
        });
        assertTrue(courseFollowController.getCourseFollowers(course1, token2)
                .stream().anyMatch(user -> user.getId().equals(user2)));
        courseFollowController.removeCourseFollower(user2, course1, token2);
        assertFalse(courseFollowController.getCourseFollowers(course1, token2)
                .stream().anyMatch(user -> user.getId().equals(user2)));
        courseFollowController.removeCourseFollower(user2, course1, token2); //Removing again is NOP
        assertFalse(courseFollowController.getCourseFollowers(course1, token2)
                .stream().anyMatch(user -> user.getId().equals(user2)));
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestUser(user1);
        testHelper.deleteTestUser(user2);
        testHelper.deleteTestCourse(course1);
        testHelper.deleteTestCourse(course2);
    }
}
