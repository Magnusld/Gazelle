package gazelle.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ModelTest {
    @Test
    public void chore() {
        Date dueDate = null;
        Post post = new Post();
        Chore chore = new Chore(1L, "Gjøre øving 1", dueDate, post);

        assertEquals(1L, chore.getKey());
        assertEquals("Gjøre øving 1", chore.getText());
        assertNull(chore.getDueDate());
        assertEquals(post, chore.getPost());

        chore.setId(2L);
        assertEquals(2l, chore.getId());

        chore.setKey(3L);
        assertNotEquals(2L, chore.getKey());
        assertEquals(3L, chore.getKey());

        chore.setText("Gjøre øving 2");
        assertNotEquals("Gjøre øving 1", chore.getText());
        assertEquals("Gjøre øving 2", chore.getText());

        Date date = new Date();
        chore.setDueDate(date);
        assertNotNull(chore.getDueDate());
        assertEquals(date, chore.getDueDate());

        Post post2 = new Post();
        post2.setId(15L);
        chore.setPost(post2);
        assertFalse(post.equals(chore.getPost()));
        assertEquals(post2, chore.getPost());

        assertTrue(chore.equals(chore));
    }

    @Test
    public void course() {
        Course course = new Course("It");
        assertThrows(ModelException.class, () -> course.validate());
        Course course1 = new Course(" itprosjekt ");
        assertThrows(ModelException.class, () -> course.validate());

        Course course2 = new Course();
        course2.setName("It-prosjekt");
        assertEquals("It-prosjekt", course2.getName());
        course2.setId(1L);
        assertEquals(1L, course2.getId());

        Set<User> followers = Collections.emptySet();
        Set<User> owners = Collections.emptySet();
        Set<Post> posts = Collections.emptySet();

        course2.setFollowers(followers);
        assertEquals(followers, course2.getFollowers());
        course2.setOwners(owners);
        assertEquals(owners, course2.getOwners());
        course2.setPosts(posts);
        assertEquals(posts, course2.getPosts());

        assertTrue(course2.equals(course2));
        assertFalse(course1.equals(course2));
    }

    @Test
    public void post() {
        Course course = new Course();
        Date startDate = new Date();
        Date endDate = new Date();
        Post post = new Post("Forelesning 1", "test description", course, startDate, endDate);

        post.setId(1L);
        assertEquals(1L, post.getId());

        assertEquals("Forelesning 1", post.getTitle());
        post.setTitle("Forelesning 2");
        assertEquals("Forelesning 2", post.getTitle());

        assertEquals("test description", post.getDescription());
        post.setDescription("this is also a test");
        assertEquals("this is also a test", post.getDescription());

        assertEquals(startDate, post.getStartDate());
        post.setStartDate(null);
        assertNull(post.getStartDate());

        assertEquals(endDate, post.getEndDate());
        post.setEndDate(null);
        assertNull(post.getEndDate());

        assertEquals(course, post.getCourse());
        post.setCourse(null);
        assertNull(post.getCourse());

        Set<Chore> chores = Collections.emptySet();
        post.setChores(chores);
        assertEquals(chores, post.getChores());

        assertEquals(post, post);
    }

    @Test
    public void tokenLogIn() {
        User user = new User();
        String token = "Bearer: validx";
        TokenLogIn tokenLogIn = new TokenLogIn(user, token);

        tokenLogIn.setId(1L);
        assertEquals(1L, tokenLogIn.getId());

        assertEquals(user, tokenLogIn.getUser());
        User user2 = new User();
        tokenLogIn.setUser(user2);
        assertEquals(user2, tokenLogIn.getUser());

        assertEquals(token, tokenLogIn.getToken());
        String token2 = "Bearer: alsovalid";
        tokenLogIn.setToken(token2);
        assertEquals(token2, tokenLogIn.getToken());

        assertEquals(tokenLogIn, tokenLogIn);
    }

    @Test
    public void userChoreProgress() {
        User user = new User();
        Chore chore = new Chore();
        UserChoreProgress.Progress progress = UserChoreProgress.Progress.FOCUSED;
        UserChoreProgress userChoreProgress = new UserChoreProgress(user, chore, progress);

        assertEquals(user, userChoreProgress.getUser());
        User user2 = new User();
        userChoreProgress.setUser(user2);
        assertEquals(user2, userChoreProgress.getUser());

        assertEquals(chore, userChoreProgress.getChore());
        Chore chore2 = new Chore();
        userChoreProgress.setChore(chore2);
        assertEquals(chore2, userChoreProgress.getChore());

        assertEquals(progress, userChoreProgress.getProgress());
        UserChoreProgress.Progress progress2 = UserChoreProgress.Progress.DONE;
        userChoreProgress.setProgress(progress2);
        assertEquals(progress2, userChoreProgress.getProgress());

        assertEquals(userChoreProgress, userChoreProgress);
    }

    @Test
    public void userChoreKey() {
        UserChoreProgress.UserChoreKey userChoreKey = new UserChoreProgress.UserChoreKey();

        userChoreKey.setUserId(1L);
        assertEquals(1L, userChoreKey.getUserId());

        userChoreKey.setChoreId(2L);
        assertEquals(2L, userChoreKey.getChoreId());

        assertEquals(userChoreKey, userChoreKey);
    }
}
