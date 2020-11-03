package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.CourseAndUserService;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class CourseAndUserEndpoint {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseAndUserService courseAndUserService;
    private final TokenAuthService tokenAuthService;

    @Autowired
    public CourseAndUserEndpoint(CourseRepository courseRepository,
                                 UserRepository userRepository,
                                 CourseAndUserService courseAndUserService,
                                 TokenAuthService tokenAuthService) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseAndUserService = courseAndUserService;
        this.tokenAuthService = tokenAuthService;
    }

    @GetMapping("/users/{userId}/follows")
    @Transactional
    public Set<Course> getFollowingCourses(@PathVariable Long userId,
                                    @RequestHeader("Authorization") @Nullable String auth) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        tokenAuthService.assertTokenForUser(user, auth);
        return user.getFollowing();
    }

    @GetMapping("/courses/{courseId}/followers")
    @Transactional
    public Set<User> getFollowerUsers(@PathVariable Long courseId,
                                           @RequestHeader("Authorization") @Nullable String auth) {
        User user = tokenAuthService.getUserObjectFromToken(auth);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        if (!courseAndUserService.isOwning(user, course))
            throw new AuthorizationException("You do not own the course");
        return course.getFollowers();
    }

    @PostMapping("/users/{userId}/follows")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addFollowingCourse(@PathVariable Long userId, @RequestBody Long courseId,
                             @RequestHeader("Authorization") @Nullable String auth) {
        tokenAuthService.assertTokenForUser(userId, auth);
        courseAndUserService.addFollower(userId, courseId);
    }

    @DeleteMapping("/users/{userId}/follows")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFollowingCourse(@PathVariable Long userId, @RequestBody Long courseId,
                             @RequestHeader("Authorization") @Nullable String auth) {
        tokenAuthService.assertTokenForUser(userId, auth);
        courseAndUserService.removeFollower(userId, courseId);
    }
}
