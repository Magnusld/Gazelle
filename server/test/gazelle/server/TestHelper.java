package gazelle.server;

import gazelle.api.*;
import gazelle.model.Chore;
import gazelle.model.Course;
import gazelle.model.Post;
import gazelle.model.User;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.PostRepository;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import gazelle.util.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TestHelper {

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PostRepository postRepository;

    public static String makeRandomString() {
        return UUID.randomUUID().toString();
    }

    public static Long makeRandomLong() {
        return new Random().nextLong();
    }

    public User createTestUserObject() {
        User user = new User(makeRandomString(), makeRandomString(),
                makeRandomString(), makeRandomString());
        return userRepository.save(user);
    }

    public Long createTestUser() {
        return createTestUserObject().getId();
    }

    /**
     * Logs a user in with its password
     *
     * @param userId the id of the user to log in
     * @return the token, including "Bearer "-prefix
     */
    public String logInUser(Long userId) {
        return TokenAuthService.addBearer(tokenAuthService.createTokenForUser(userId));
    }

    @Transactional
    public void deleteTestUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public void deleteTestUser(User user) {
        deleteTestUser(user.getId());
    }

    /**
     * Creates a Course without any owners, followers or posts
     *
     * @return the course object
     */
    public Course createTestCourseObject() {
        Course course = new Course(makeRandomString());
        return courseRepository.save(course);
    }

    public Long createTestCourse() {
        return createTestCourseObject().getId();
    }

    @Transactional
    public void deleteTestCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    public void deleteTestCourse(Course course) {
        deleteTestCourse(course.getId());
    }

    /**
     * Creates a Post without anything
     *
     * @return the posts object
     */
    public Post createTestPostObject(Course course) {
        Post post = new Post(makeRandomString(), makeRandomString(), course,
                new Date(), new Date());
        return postRepository.save(post);
    }

    public Long createTestPost(Course course) {
        return createTestPostObject(course).getId();
    }

    @Transactional
    public void deleteTestPost(Long postId) {
        postRepository.deleteById(postId);
    }

    public void deleteTestPost(Post post) {
        deleteTestCourse(post.getId());
    }

    public NewPostRequest createTestNewPostRequest() {
        List<NewChoreRequest> chores = new ArrayList<>();
        chores.add(createTestNewChoreRequest());
        chores.add(createTestNewChoreRequest());

        NewPostRequest.Builder builder = new NewPostRequest.Builder();
        builder.title(makeRandomString());
        builder.description(makeRandomString());
        builder.startDate(DateHelper.localDateOfDate(new Date()));
        builder.endDate(DateHelper.localDateOfDate(new Date()));
        builder.chores(chores);

        return builder.build();
    }

    public NewChoreRequest createTestNewChoreRequest() {
        return new NewChoreRequest(null, 5000L, makeRandomString(),
                DateHelper.localDateOfDate(new Date()));
    }
}
