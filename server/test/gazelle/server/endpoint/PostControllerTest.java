package gazelle.server.endpoint;

import gazelle.api.*;
import gazelle.model.Course;
import gazelle.model.Post;
import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.error.*;
import gazelle.server.service.CourseAndUserService;
import gazelle.util.DateHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PostControllerTest {

    @Autowired
    private CourseController courseController;

    @Autowired
    private PostController postController;

    @Autowired
    private CourseAndUserService courseAndUserService;

    @Autowired
    private TestHelper testHelper;

    private Post post1;
    private Post post2;
    private Course course1;
    private Course course2;
    private Long user1;
    private String token1;

    @BeforeAll
    public void setup() {
        course1 = testHelper.createTestCourseObject();
        course2 = testHelper.createTestCourseObject();
        post1 = testHelper.createTestPostObject(course1);
        post2 = testHelper.createTestPostObject(course2);
        user1 = testHelper.createTestUser();
        token1 = testHelper.logInUser(user1);
        courseAndUserService.addOwner(user1, course2.getId());
    }

    //@Test
    public void makePostResponse() {
        User user = testHelper.createTestUserObject();
        String token = testHelper.logInUser(user.getId());

        PostResponse postResponse = postController.makePostResponse(post1, user);

        assertEquals(postResponse.getId(), post1.getId());
        assertEquals(postResponse.getDescription(), post1.getDescription());
        assertEquals(postResponse.getTitle(), post1.getTitle());
        assertEquals(postResponse.getStartDate(), DateHelper.localDateOfDate(post1.getStartDate()));
        assertEquals(postResponse.getEndDate(), DateHelper.localDateOfDate(post1.getEndDate()));
    }

    @Test
    public void getPostContent() {
        assertThrows(PostNotFoundException.class, () -> {
            postController.getPostContent(5000L, null);
        });

        PostContentResponse response = postController.getPostContent(post1.getId(), null);
        assertEquals(post1.getId(), response.getId());

        response = postController.getPostContent(post1.getId(), token1);
        assertEquals(post1.getId(), response.getId());
        assertEquals(post1.getTitle(), response.getTitle());
        assertEquals(post1.getCourse().getId(), response.getCourseId());
        assertEquals(post1.getDescription(), response.getDescription());

        response = postController.getPostContent(post2.getId(), token1);
        assertEquals(post2.getId(), response.getId());
        assertEquals(post2.getTitle(), response.getTitle());
        assertEquals(post2.getCourse().getId(), response.getCourseId());
        assertEquals(post2.getDescription(), response.getDescription());
    }

    @Test
    public void addNewPost() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);

        NewCourseRequest course = new NewCourseRequest("Testname");
        CourseResponse courseResponse = courseController.addNewCourse(course, token);

        NewPostRequest post = testHelper.createTestNewPostRequest();
        assertThrows(InvalidTokenException.class, () -> {
            postController.addNewPost(course2.getId(), post, "Bearer: dummy");
        });
        assertThrows(AuthorizationException.class, () -> {
            PostContentResponse response = postController.addNewPost(course1.getId(), post, token);
        });
        PostContentResponse response = postController.addNewPost(
                courseResponse.getId(), post, token);
        assertNotNull(response.getId());
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals(true, response.getIsOwning());

        //Check that user is an owner
        assertTrue(courseAndUserService.isOwning(user, courseResponse.getId()));
        testHelper.deleteTestPost(response.getId());
        testHelper.deleteTestCourse(courseResponse.getId());
        testHelper.deleteTestUser(user);
    }

    @Test
    public void updateExistingPost() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);

        NewCourseRequest course = new NewCourseRequest("Testname");
        CourseResponse courseResponse = courseController.addNewCourse(course, token);

        NewPostRequest originalPost = testHelper.createTestNewPostRequest();
        NewPostRequest updatedPost = testHelper.createTestNewPostRequest();

        PostContentResponse original = postController.addNewPost(
                courseResponse.getId(), originalPost, token);
        PostContentResponse updated = postController.updateExistingPost(
                original.getId(), updatedPost, token);

        assertEquals(original.getId(), updated.getId());
        assertEquals(original.getCourseId(), updated.getCourseId());
        assertNotEquals(original.getTitle(), updated.getTitle());
        assertNotEquals(original.getDescription(), updated.getDescription());

        updatedPost.setChores(originalPost.getChores());
        PostContentResponse updated2 = postController.updateExistingPost(
                original.getId(), updatedPost, token);

        //Check that user is an owner
        assertTrue(courseAndUserService.isOwning(user, courseResponse.getId()));
        testHelper.deleteTestPost(original.getId());
        testHelper.deleteTestCourse(courseResponse.getId());
        testHelper.deleteTestUser(user);
    }

    @Test
    public void deletePost() {
        Long user = testHelper.createTestUser();
        String token = testHelper.logInUser(user);

        assertThrows(PostNotFoundException.class, () -> {
            postController.deletePost(5000L, token);
        });

        NewCourseRequest course = new NewCourseRequest("Testname");
        CourseResponse courseResponse = courseController.addNewCourse(course, token);
        NewPostRequest post = testHelper.createTestNewPostRequest();

        assertThrows(AuthorizationException.class, () -> {
            PostContentResponse response = postController.addNewPost(course1.getId(), post, token);
        });

        PostContentResponse response = postController.addNewPost(
                courseResponse.getId(), post, token);

        assertThrows(MissingAuthorizationException.class, () -> {
            postController.deletePost(response.getId(), null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            postController.deletePost(response.getId(), "Bearer: dummy");
        });
        assertThrows(AuthorizationException.class, () -> {
            postController.deletePost(response.getId(), token1);
        });

        postController.deletePost(response.getId(), token);

        testHelper.deleteTestUser(user);
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestCourse(course1);
        testHelper.deleteTestCourse(course2);
        testHelper.deleteTestUser(user1);
    }
}
