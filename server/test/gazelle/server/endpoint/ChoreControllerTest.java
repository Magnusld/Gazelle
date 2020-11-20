package gazelle.server.endpoint;

import gazelle.api.*;
import gazelle.model.UserChoreProgress;
import gazelle.model.UserChoreProgress.Progress;
import gazelle.server.TestHelper;
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

    private PostContentResponse postContentResponse;
    private Long user;
    private String token;

    @BeforeAll
    public void setup() {
        user = testHelper.createTestUser();
        token = testHelper.logInUser(user);

        NewCourseRequest course = new NewCourseRequest("Testname");
        CourseResponse courseResponse = courseController.addNewCourse(course, token);

        NewPostRequest post = testHelper.createTestNewPostRequest();
        postContentResponse = postController.addNewPost(courseResponse.getId(), post, token);
    }

    @Test
    public void setChoreState() {
        assertThrows(UnprocessableEntityException.class, () -> {
            choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                    user,
                    new ValueWrapper<>(null),
                    token);
        });

        choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                user,
                new ValueWrapper<>(Progress.FOCUSED),
                token);
        postContentResponse = postController.getPostContent(postContentResponse.getId(), token);
        assertEquals(postContentResponse.getChores().get(0).getProgress(), Progress.FOCUSED);

        choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                user,
                new ValueWrapper<>(Progress.UNDONE),
                token);
        postContentResponse = postController.getPostContent(postContentResponse.getId(), token);
        assertEquals(postContentResponse.getChores().get(0).getProgress(), Progress.UNDONE);

        choreController.setChoreState(postContentResponse.getChores().iterator().next().getId(),
                user,
                new ValueWrapper<>(UserChoreProgress.Progress.DONE),
                token);
        postContentResponse = postController.getPostContent(postContentResponse.getId(), token);
        assertEquals(postContentResponse.getChores().get(0).getProgress(), Progress.DONE);

        testHelper.deleteTestUser(user);
    }

    @Test
    public void getFocusedChores() {
        choreController.setChoreState(postContentResponse.getChores().get(0).getId(),
                user,
                new ValueWrapper<>(Progress.UNDONE),
                token);
        choreController.setChoreState(postContentResponse.getChores().get(1).getId(),
                user,
                new ValueWrapper<>(Progress.UNDONE),
                token);
        List<ChoreFullResponse> response = choreController.getFocusedChores(user, token);
        assertEquals(0, response.size());

        choreController.setChoreState(postContentResponse.getChores().get(0).getId(),
                user,
                new ValueWrapper<>(Progress.FOCUSED),
                token);
        response = choreController.getFocusedChores(user, token);
        assertEquals(1, response.size());

        choreController.setChoreState(postContentResponse.getChores().get(1).getId(),
                user,
                new ValueWrapper<>(Progress.FOCUSED),
                token);
        response = choreController.getFocusedChores(user, token);
        assertEquals(2, response.size());
    }

}
