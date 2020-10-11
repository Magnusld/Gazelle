package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.CourseRole.CourseRoleType;
import gazelle.model.CourseRoleKey;
import gazelle.model.User;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.GazelleException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.CourseRoleRepository;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseRoleController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseRoleRepository courseRoleRepository;

    @Autowired
    public CourseRoleController(UserRepository userRepository,
                                CourseRepository courseRepository,
                                CourseRoleRepository courseRoleRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseRoleRepository = courseRoleRepository;
    }

    @GetMapping("/users/{userId}/courses/{courseId}/role")
    public CourseRole getCourseRole(@PathVariable Long userId,
                                    @PathVariable Long courseId) {
        CourseRoleKey key = new CourseRoleKey(userId, courseId);
        return courseRoleRepository.findById(key)
                .orElseThrow(() ->
                        new GazelleException("Course role not found", null, HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/{userId}/courses")
    public List<Course> findCoursesForUser(@PathVariable Long userId,
                                           @RequestParam(required=false) CourseRoleType role) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (role == null)
            return courseRepository.findAllByUser(user);
        else
            return courseRepository.findAllByUserWithRole(user, role);
    }

    @PutMapping("/users/{userId}/courses/{courseId}/role")
    public ResponseEntity<Void> setCourseRole(@PathVariable Long userId,
                                        @PathVariable Long courseId,
                                        @RequestBody CourseRoleType role) {

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        boolean existed = courseRoleRepository.existsByUserAndCourse(user, course);

        CourseRole courseRole = new CourseRole(user, course, role);
        courseRoleRepository.save(courseRole);

        return new ResponseEntity<>(existed ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
    }
}
