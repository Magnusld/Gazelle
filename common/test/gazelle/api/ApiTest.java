package gazelle.api;

import gazelle.model.Chore;
import gazelle.model.UserChoreProgress;
import net.bytebuddy.asm.Advice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ApiTest {
    @Test
    public void choreFullResponse() {
        ChoreFullResponse.Builder builder = new ChoreFullResponse.Builder();
        LocalDate dueDate = LocalDate.now();
        UserChoreProgress.Progress progress = UserChoreProgress.Progress.FOCUSED;
        builder
                .id(1L).key(2L).text("Gjøre øving 1")
                .dueDate(dueDate).progress(progress)
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

        assertEquals(choreFullResponse, choreFullResponse);
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

        assertEquals(choreResponse, choreResponse);
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

        assertEquals(courseContentResponse, courseContentResponse);
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

        assertEquals(courseResponse, courseResponse);
    }

    @Test
    public void newChoreRequest() {
        Long id = 1L;
        Long key = 2L;
        String text = "test";
        LocalDate dueDate = LocalDate.now().plusDays(1);
        NewChoreRequest newChoreRequest = new NewChoreRequest(id, key, text, dueDate);
        newChoreRequest.validate();
        
        assertEquals(id, newChoreRequest.getId());
        newChoreRequest.setId(11L);
        assertNotEquals(newChoreRequest.getId(), id);
        assertEquals(newChoreRequest.getId(), 11L);

        assertEquals(key, newChoreRequest.getKey());
        newChoreRequest.setKey(22L);
        assertNotEquals(newChoreRequest.getKey(), key);
        assertEquals(newChoreRequest.getKey(), 22L);

        assertEquals(text, newChoreRequest.getText());
        newChoreRequest.setText("test2");
        assertNotEquals(newChoreRequest.getText(), text);
        assertEquals(newChoreRequest.getText(), "test2");

        assertEquals(dueDate, newChoreRequest.getDueDate());
        newChoreRequest.setDueDate(LocalDate.now().plusWeeks(2));
        assertNotEquals(dueDate, newChoreRequest.getDueDate());
        assertEquals(LocalDate.now().plusWeeks(2), newChoreRequest.getDueDate());

        assertEquals(newChoreRequest,newChoreRequest);
    }

    @Test
    public void newCourseRequest() {
        String name = "test";
        NewCourseRequest newCourseRequest = new NewCourseRequest(name);

        assertEquals(name, newCourseRequest.getName());
        newCourseRequest.setName("test2");
        assertNotEquals(name, newCourseRequest.getName());
        assertEquals(newCourseRequest.getName(), "test2");

        assertEquals(newCourseRequest, newCourseRequest);
    }

    @Test
    public void newPostRequest() {
        NewPostRequest.Builder builder = new NewPostRequest.Builder();
        List<NewChoreRequest> chores = new ArrayList<>();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusWeeks(2);
        builder
                .title("test").description("test description")
                .startDate(startDate).endDate(endDate)
                .chores(chores);
        NewPostRequest newPostRequest = builder.build();

        assertEquals("test", newPostRequest.getTitle());
        assertEquals("test description", newPostRequest.getDescription());
        assertEquals(startDate, newPostRequest.getStartDate());
        assertEquals(endDate, newPostRequest.getEndDate());
        assertEquals(chores, newPostRequest.getChores());

        assertEquals(newPostRequest, newPostRequest);
    }

    @Test
    public void postContentResponse() {
        PostContentResponse.Builder builder = new PostContentResponse.Builder();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusWeeks(2);
        List<ChoreResponse> chores = new ArrayList<>();
        builder
                .id(1L).title("test").description("test description")
                .startDate(startDate).endDate(endDate)
                .courseId(11L).courseName("courseTest").isOwning(false).chores(chores);
        PostContentResponse postContentResponse = builder.build();

        assertEquals(1L, postContentResponse.getId());
        assertEquals("test", postContentResponse.getTitle());
        assertEquals("test description", postContentResponse.getDescription());
        assertEquals(startDate, postContentResponse.getStartDate());
        assertEquals(endDate, postContentResponse.getEndDate());
        assertEquals(11L, postContentResponse.getCourseId());
        assertEquals("courseTest",postContentResponse.getCourseName());
        assertEquals(false, postContentResponse.getIsOwning());
        assertEquals(chores, postContentResponse.getChores());

        assertEquals(postContentResponse, postContentResponse);
    }

    @Test
    public void postResponse() {
        PostResponse.Builder builder = new PostResponse.Builder();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusWeeks(2);
        ChoreResponse nextChoreDue = new ChoreResponse();
        builder
                .id(1L).title("test").description("test description")
                .startDate(startDate).endDate(endDate)
                .nextChoreDue(nextChoreDue).choresDone(10).choresFocused(3).choresCount(5);
        PostResponse postResponse = builder.build();

        assertEquals(1L, postResponse.getId());
        assertEquals("test", postResponse.getTitle());
        assertEquals("test description", postResponse.getDescription());
        assertEquals(startDate, postResponse.getStartDate());
        assertEquals(endDate, postResponse.getEndDate());
        assertEquals(nextChoreDue, postResponse.getNextChoreDue());
        assertEquals(10, postResponse.getChoresDone());
        assertEquals(3, postResponse.getChoresFocused());
        assertEquals(5, postResponse.getChoresCount());

        assertEquals(postResponse, postResponse);
    }

    @Test
    public void userResponse() {
        UserResponse.Builder builder = new UserResponse.Builder();
        builder.id(1L).firstName("Per").lastName("Johansen");
        UserResponse userResponse = builder.build();

        assertEquals(1L, userResponse.getId());
        assertEquals("Per", userResponse.getFirstName());
        assertEquals("Johansen", userResponse.getLastName());

        assertEquals(userResponse, userResponse);
        assertFalse(userResponse.equals((int) 3));
    }

    @Test
    public void valueWrapper() {
        ValueWrapper valueWrapper = new ValueWrapper(3);
        assertEquals(3, valueWrapper.getValue());
        valueWrapper.setValue("string");
        assertNotEquals(3, valueWrapper.getValue());
        assertEquals("string", valueWrapper.getValue());
    }
}
