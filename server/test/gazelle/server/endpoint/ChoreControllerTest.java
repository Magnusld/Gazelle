package gazelle.server.endpoint;

import gazelle.api.*;
import gazelle.model.Course;
import gazelle.model.Post;
import gazelle.model.User;
import gazelle.model.UserChoreProgress;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.UnprocessableEntityException;
import gazelle.server.service.CourseAndUserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChoreControllerTest {

    @Autowired
    private CourseController courseController;

    @Autowired
    private PostController postController;

    @Autowired
    private ChoreController choreController;

    @Autowired
    private CourseAndUserService courseAndUserService;

    @Autowired
    private TestHelper testHelper;

    private NewPostRequest post;
    private NewCourseRequest course;
    private CourseResponse courseResponse;
    private PostContentResponse postContentResponse;
    private User user;
    private String token;

    @BeforeAll
    public void setup() {
        user = testHelper.createTestUserObject();
        token = testHelper.logInUser(user.getId());

        course = new NewCourseRequest("Testname");
        courseResponse = courseController.addNewCourse(course, token);

        post = testHelper.createTestNewPostRequest();
        postContentResponse = postController.addNewPost(courseResponse.getId(), post, token);
    }

    @Test
    public void setChoreState() {
        assertThrows(UnprocessableEntityException.class, () -> {
            choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                    user.getId(),
                    new ValueWrapper<>(null),
                    token);
        });

        choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.FOCUSED),
                token);
        postContentResponse = postController.getPostContent(postContentResponse.getId(), token);
        assertEquals(postContentResponse.getChores().get(0).getProgress(), UserChoreProgress.Progress.FOCUSED);

        choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.UNDONE),
                token);
        postContentResponse = postController.getPostContent(postContentResponse.getId(), token);
        assertEquals(postContentResponse.getChores().get(0).getProgress(), UserChoreProgress.Progress.UNDONE);

        choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.DONE),
                token);
        postContentResponse = postController.getPostContent(postContentResponse.getId(), token);
        assertEquals(postContentResponse.getChores().get(0).getProgress(), UserChoreProgress.Progress.DONE);

        testHelper.deleteTestUser(user);
    }

    @Test
    public void getFocusedChores() {
        choreController.setChoreState(postContentResponse.getChores().get(0).getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.UNDONE),
                token);
        choreController.setChoreState(postContentResponse.getChores().get(1).getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.UNDONE),
                token);
        List<ChoreFullResponse> response = choreController.getFocusedChores(user.getId(), token);
        assertEquals(0, response.size());

        choreController.setChoreState(postContentResponse.getChores().get(0).getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.FOCUSED),
                token);
        response = choreController.getFocusedChores(user.getId(), token);
        assertEquals(1, response.size());

        choreController.setChoreState(postContentResponse.getChores().get(1).getId(),
                user.getId(),
                new ValueWrapper<>(UserChoreProgress.Progress.FOCUSED),
                token);
        response = choreController.getFocusedChores(user.getId(), token);
        assertEquals(2, response.size());
    }

}
