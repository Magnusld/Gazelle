package gazelle.server.service;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseAndUserServiceTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private CourseAndUserService courseAndUserService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    public void followerTest() {
        final User user1 = testHelper.createTestUserObject();
        final User user2 = testHelper.createTestUserObject();

        final Course course1 = testHelper.createTestCourseObject();

        assertFalse(courseAndUserService.isFollowing(user1, course1));
        courseAndUserService.addFollower(user1, course1);
        assertTrue(courseAndUserService.isFollowing(user1, course1));
        assertFalse(courseAndUserService.isFollowing(user2, course1));

        assertEquals(1, user1.getFollowing().size());
        assertEquals(1, course1.getFollowers().size());
        assertTrue(user1.getFollowing().contains(course1));
        assertTrue(course1.getFollowers().contains(user1));

        courseAndUserService.addFollower(user2, course1);
        assertEquals(2, course1.getFollowers().size());

        assertTrue(courseAndUserService.removeFollower(user1, course1));
        assertFalse(courseAndUserService.removeFollower(user1, course1));

        assertTrue(user1.getFollowing().isEmpty());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start(); //We commit the changes to check the DB round trip

        User dbUser1 = userRepository.findById(user1.getId())
                .orElseThrow(UserNotFoundException::new);
        User dbUser2 = userRepository.findById(user2.getId())
                .orElseThrow(UserNotFoundException::new);
        assertTrue(dbUser1.getFollowing().isEmpty());
        assertEquals(1, dbUser2.getFollowing().size());
        assertTrue(dbUser2.getFollowing().contains(course1));

        Course dbCourse1 = courseRepository.findById(course1.getId())
                .orElseThrow(CourseNotFoundException::new);
        assertTrue(dbCourse1.getFollowers().contains(user2));
        assertTrue(dbCourse1.getFollowers().contains(dbUser2)); //Same id

        testHelper.deleteTestUser(user1.getId());
        testHelper.deleteTestUser(user2.getId());
        testHelper.deleteTestCourse(course1.getId());
    }

    @Test
    @Transactional
    public void ownerTest() {
        final User user1 = testHelper.createTestUserObject();
        final User user2 = testHelper.createTestUserObject();

        final Course course1 = testHelper.createTestCourseObject();

        assertFalse(courseAndUserService.isOwning(user1, course1));
        courseAndUserService.addOwner(user1, course1);
        assertTrue(courseAndUserService.isOwning(user1, course1));
        assertFalse(courseAndUserService.isOwning(user2, course1));

        assertEquals(1, user1.getOwning().size());
        assertEquals(1, course1.getOwners().size());
        assertTrue(user1.getOwning().contains(course1));
        assertTrue(course1.getOwners().contains(user1));

        courseAndUserService.addOwner(user2, course1);
        assertEquals(2, course1.getOwners().size());

        assertTrue(courseAndUserService.removeOwner(user1, course1));
        assertFalse(courseAndUserService.removeOwner(user1, course1));

        assertTrue(user1.getOwning().isEmpty());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start(); //We commit the changes to check the DB round trip

        User dbUser1 = userRepository.findById(user1.getId())
                .orElseThrow(UserNotFoundException::new);
        User dbUser2 = userRepository.findById(user2.getId())
                .orElseThrow(UserNotFoundException::new);
        assertTrue(dbUser1.getOwning().isEmpty());
        assertEquals(1, dbUser2.getOwning().size());
        assertTrue(dbUser2.getOwning().contains(course1));

        Course dbCourse1 = courseRepository.findById(course1.getId())
                .orElseThrow(CourseNotFoundException::new);
        assertTrue(dbCourse1.getOwners().contains(user2));
        assertTrue(dbCourse1.getOwners().contains(dbUser2)); //Same id

        testHelper.deleteTestUser(user1.getId());
        testHelper.deleteTestUser(user2.getId());
        testHelper.deleteTestCourse(course1.getId());
    }

    @Test
    @Transactional
    public void deleteTest() {
        User user1 = testHelper.createTestUserObject();
        User user2 = testHelper.createTestUserObject();
        Course course1 = testHelper.createTestCourseObject();

        courseAndUserService.addFollower(user1, course1);
        courseAndUserService.addFollower(user2, course1);

        assertEquals(2, course1.getFollowers().size());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start(); //We commit the changes to check the DB round trip

        course1 = courseRepository.findById(course1.getId())
                .orElseThrow(CourseNotFoundException::new);
        user1 = userRepository.findById(user1.getId())
                .orElseThrow(UserNotFoundException::new);

        testHelper.deleteTestUser(user1.getId());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start(); //We commit the changes to check the DB round trip

        course1 = courseRepository.findById(course1.getId())
                .orElseThrow(CourseNotFoundException::new);
        user2 = userRepository.findById(user2.getId())
                .orElseThrow(UserNotFoundException::new);

        //Now the user has been deleted
        assertEquals(1, course1.getFollowers().size());

        testHelper.deleteTestCourse(course1.getId());

        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start(); //We commit the changes to check the DB round trip

        user2 = userRepository.findById(user2.getId())
                .orElseThrow(UserNotFoundException::new);

        assertTrue(user2.getFollowing().isEmpty());
    }
}
