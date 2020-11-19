package gazelle.api;

import gazelle.model.Chore;
import gazelle.model.UserChoreProgress;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ApiTest {
    @Test
    public void choreFullResponse() {
        ChoreFullResponse.Builder builder = new ChoreFullResponse.Builder();
        builder
                .id(1L).key(2L).text("Gjøre øving 1")
                .postId(1L).postTitle("Forelesning 1")
                .courseId(1L).courseName("It-prosjekt");
        ChoreFullResponse choreFullResponse = builder.build();

        assertEquals(1, choreFullResponse.getId());
        assertEquals(2, choreFullResponse.getKey());
        assertEquals("Gjøre øving 1", choreFullResponse.getText());
        assertEquals(1, choreFullResponse.getPostId());
        assertEquals("Forelesning 1", choreFullResponse.getPostTitle());
        assertEquals(1, choreFullResponse.getCourseId());
        assertEquals("It-prosjekt", choreFullResponse.getCourseName());

        assertTrue(choreFullResponse.equals(choreFullResponse));
    }

    @Test
    public void choreResponse() {
        ChoreResponse.Builder builder = new ChoreResponse.Builder();
        LocalDate dueDate = LocalDate.now().plusDays(1);
        UserChoreProgress.Progress progress = UserChoreProgress.Progress.FOCUSED;
        builder.id(1L).key(2L).text("testTekst").dueDate(dueDate).progress(progress);
        ChoreResponse choreResponse = builder.build();

        assertEquals(1L, choreResponse.getId());
        assertEquals(2L, choreResponse.getKey());
        assertEquals("testTekst", choreResponse.getText());
        assertEquals(dueDate, choreResponse.getDueDate());
        assertEquals(progress, choreResponse.getProgress());

        assertTrue(choreResponse.equals(choreResponse));
        assertFalse(choreResponse.equals(null));
    }

    @Test
    public void courseContentResponse() {
        CourseContentResponse.Builder builder = new CourseContentResponse.Builder();
        List<PostResponse> posts = new ArrayList<>();
        builder.id(1L).name("test").isFollower(true).isOwner(false).posts(posts);
        CourseContentResponse courseContentResponse = builder.build();

        assertEquals(1L, courseContentResponse.getId());
        assertEquals("test", courseContentResponse.getName());
        assertEquals(true, courseContentResponse.getIsFollower());
        assertEquals(false, courseContentResponse.getIsOwner());
        assertEquals(posts, courseContentResponse.getPosts());

        assertTrue(courseContentResponse.equals(courseContentResponse));
        assertFalse(courseContentResponse.equals(null));
    }

    @Test
    public void courseResponse() {
        CourseResponse.Builder builder = new CourseResponse.Builder();
        PostResponse currentPost = new PostResponse();
        PostResponse nextPost = new PostResponse();
        PostResponse previousPost = new PostResponse();
        ChoreResponse nextChoreDue = new ChoreResponse();
        builder
                .id(1L).name("test").isOwner(true).isFollower(false)
                .currentPost(currentPost).nextPost(nextPost).previousPost(previousPost).nextChoreDue(nextChoreDue);
        CourseResponse courseResponse = builder.build();

        assertEquals(1L, courseResponse.getId());
        assertEquals("test", courseResponse.getName());
        assertEquals(true, courseResponse.getIsOwner());
        assertEquals(false, courseResponse.getIsFollower());
        assertEquals(currentPost, courseResponse.getCurrentPost());
        assertEquals(nextPost, courseResponse.getNextPost());
        assertEquals(previousPost, courseResponse.getPreviousPost());
        assertEquals(nextChoreDue, courseResponse.getNextChoreDue());

        assertTrue(courseResponse.equals(courseResponse));
        assertFalse(courseResponse.equals(null));
    }


}
