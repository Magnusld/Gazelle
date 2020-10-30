package gazelle.server.service;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.TestHelper;
import org.junit.jupiter.api.AfterAll;
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
public class CourseAndUserServiceTest {

    @Autowired
    private CourseAndUserService courseAndUserService;
    @Autowired
    private TestHelper testHelper;

    private User user1;
    private User user2;
    private Course course1;
    private Course course2;

    @BeforeAll
    public void setup() {
        user1 = testHelper.createTestUser();
        user2 = testHelper.createTestUser();
        course1 = testHelper.createTestCourse();
        course2 = testHelper.createTestCourse();
    }

    @Test
    public void followerTest() {
        assertTrue(course1.getFollowers().isEmpty());
        courseAndUserService.addFollower(user1, course1);
        assertTrue(course1.getFollowers().contains(user1));
        assertFalse(course1.getFollowers().contains(user2));
        assertTrue(user1.getFollowing().contains(course1));
        assertTrue(courseAndUserService.isFollower(user1, course1));
        assertFalse(user1.getFollowing().contains(course2));
        assertFalse(courseAndUserService.isFollower(user1, course2));
        courseAndUserService.addFollower(user1, course2);
        assertTrue(user1.getFollowing().contains(course2));
        assertTrue(courseAndUserService.isFollower(user1, course2));
        courseAndUserService.addFollower(user1, course2); //Add again
        assertEquals(2, user1.getFollowing().size());
        assertEquals(1, course2.getFollowers().size());

        assertFalse(courseAndUserService.removeFollower(user2, course2));
        assertTrue(courseAndUserService.removeFollower(user1, course2));
        assertFalse(courseAndUserService.removeFollower(user1, course2));
        assertEquals(1, user1.getFollowing().size());
        assertEquals(0, course2.getFollowers().size());

        courseAndUserService.removeFollower(user1, course1);
        courseAndUserService.removeFollower(user2, course1);
    }

    @Test
    public void ownerTest() {
        assertTrue(course1.getOwners().isEmpty());
        courseAndUserService.addOwner(user1, course1);
        assertTrue(course1.getOwners().contains(user1));
        assertFalse(course1.getOwners().contains(user2));
        assertTrue(user1.getOwning().contains(course1));
        assertTrue(courseAndUserService.isOwner(user1, course1));
        assertFalse(user1.getOwning().contains(course2));
        assertFalse(courseAndUserService.isOwner(user1, course2));
        courseAndUserService.addOwner(user1, course2);
        assertTrue(user1.getOwning().contains(course2));
        assertTrue(courseAndUserService.isOwner(user1, course2));
        courseAndUserService.addOwner(user1, course2); //Add again
        assertEquals(2, user1.getOwning().size());
        assertEquals(1, course2.getOwners().size());

        assertFalse(courseAndUserService.removeOwner(user2, course2));
        assertTrue(courseAndUserService.removeOwner(user1, course2));
        assertFalse(courseAndUserService.removeOwner(user1, course2));
        assertEquals(1, user1.getOwning().size());
        assertEquals(0, course2.getOwners().size());

        courseAndUserService.removeOwner(user1, course1);
        courseAndUserService.removeOwner(user2, course1);
    }

    @Test
    public void bothTest() {
        assertFalse(courseAndUserService.isFollower(user1, course1));
        assertFalse(courseAndUserService.isOwner(user1, course1));
        courseAndUserService.addFollower(user1, course1);
        courseAndUserService.addOwner(user1, course1);
        assertTrue(courseAndUserService.isFollower(user1, course1));
        assertTrue(courseAndUserService.isOwner(user1, course1));
        courseAndUserService.removeFollower(user1, course1);
        courseAndUserService.removeOwner(user1, course1);
    }

    @Test
    public void deletionTest() {
        //What happens when one part of the relation is deleted?

        courseAndUserService.addFollower(user1, course1);
        courseAndUserService.addFollower(user1, course2);
        courseAndUserService.addOwner(user1, course1);
        courseAndUserService.addOwner(user2, course1);
        courseAndUserService.addOwner(user2, course2);

        testHelper.deleteTestCourse(course1);

        assertEquals(1, user1.getFollowing().size());
        assertEquals(0, user1.getOwning().size());
        assertEquals(1, user2.getOwning().size());

        course1 = testHelper.createTestCourse();

        testHelper.deleteTestUser(user1);

        assertEquals(0, course2.getFollowers().size());
        assertEquals(1, course2.getOwners().size());

        user1 = testHelper.createTestUser();

        courseAndUserService.removeOwner(user2, course2);
    }

    @Test
    public void multipleObjectsTest() {

    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestUser(user1);
        testHelper.deleteTestUser(user2);
    }
}
