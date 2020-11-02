package gazelle.server;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.endpoint.CourseController;
import gazelle.server.endpoint.LoginEndpoint;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TestHelper {

    @Autowired
    private TokenAuthService tokenAuthService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public static String makeRandomString() {
        return UUID.randomUUID().toString();
    }

    public User createTestUserObject() {
        User user = new User(makeRandomString(), makeRandomString());
        return userRepository.save(user);
    }

    @Transactional
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

    @Transactional
    public void deleteTestCourse(Long courseId) {
        courseRepository.deleteById(courseId);
    }

    public void deleteTestCourse(Course course) {
        deleteTestCourse(course.getId());
    }
}
