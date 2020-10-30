package gazelle.server;

import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.endpoint.LoginEndpoint;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestHelper {

    @Autowired
    private LoginEndpoint loginEndpoint;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;

    public String randString() {
        return UUID.randomUUID().toString();
    }

    public User createTestUser() {
        String name = randString();
        User user = new User(name, name);
        return userRepository.save(user);
    }

    public LogInResponse createLoggedInTestUser() {
        String name = randString();
        return loginEndpoint.signup(new SignUpRequest(name, name));
    }

    public void deleteTestUser(User user) {
        userRepository.delete(user);
    }

    public Course createTestCourse() {
        Course course = new Course(randString());
        return courseRepository.save(course);
    }

    public void deleteTestCourse(Course course) {
        courseRepository.delete(course);
    }
}
