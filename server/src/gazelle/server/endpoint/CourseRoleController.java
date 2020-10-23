package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.CourseRole.CourseRoleType;
import gazelle.model.User;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.GazelleException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.service.CourseRoleService;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseRoleController {

    private final CourseRoleService courseRoleService;
    private final CourseRepository courseRepository;
    private final TokenAuthService tokenAuthService;

    @Autowired
    public CourseRoleController(CourseRoleService courseRoleService,
                                CourseRepository courseRepository,
                                TokenAuthService tokenAuthService) {
        this.courseRoleService = courseRoleService;
        this.courseRepository = courseRepository;
        this.tokenAuthService = tokenAuthService;
    }

    /**
     * Gets the role of the specified user in the specified course
     *
     * @param userId the id of the user
     * @param courseId the id of the course
     * @param auth the authorization header
     * @return the role the user has in the course
     * @throws GazelleException 404 (Not found) if the user doesn't have a role in the course
     */
    @GetMapping("/users/{userId}/courses/{courseId}/role")
    public CourseRoleType getCourseRole(@PathVariable Long userId,
                                        @PathVariable Long courseId,
                                        @RequestHeader("Authorization") String auth) {
        User user = tokenAuthService.assertUserLoggedIn(userId, auth);
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        CourseRoleType role =  courseRoleService.getCourseRole(user, course);
        if (role == null)
            throw new GazelleException("No role", null, HttpStatus.NOT_FOUND);
        return role;
    }

    /**
     * Gets all courses where the user has a specified role (or any role if null)
     *
     * @param userId the id of the user
     * @param role the role filter, or null if we want any role
     * @param auth the authorization header
     * @return all courses where the user has the specified role (or any role if null)
     */
    @GetMapping("/users/{userId}/courses")
    public List<Course> findCoursesForUser(@PathVariable Long userId,
                                           @RequestParam(required = false) CourseRoleType role,
                                           @RequestHeader("Authorization") String auth) {
        User user = tokenAuthService.assertUserLoggedIn(userId, auth);
        return courseRoleService.getCoursesForUser(user, role);
    }

    /**
     * Sets the role of the user in the course
     *
     * @param userId the id of the user
     * @param courseId the id of the course
     * @param role the desired role
     * @param auth the authorization header
     * @return 201 (Created) if new, 204 (No content) if the role replaces a previous role
     */
    @PutMapping("/users/{userId}/courses/{courseId}/role")
    public ResponseEntity<Void> setCourseRole(@PathVariable Long userId,
                                              @PathVariable Long courseId,
                                              @RequestBody CourseRoleType role,
                                              @RequestHeader("Authorization") String auth) {
        User user = tokenAuthService.assertUserLoggedIn(userId, auth);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);

        boolean existed = courseRoleService.setRole(user, course, role);

        return new ResponseEntity<>(existed ? HttpStatus.NO_CONTENT : HttpStatus.CREATED);
    }
}
