package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.ExistingEntityException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.service.CourseRoleService;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final CourseRoleService courseRoleService;
    private final TokenAuthService tokenAuthService;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            CourseRoleService courseRoleService,
                            TokenAuthService tokenAuthService) {
        this.courseRepository = courseRepository;
        this.courseRoleService = courseRoleService;
        this.tokenAuthService = tokenAuthService;
    }

    @GetMapping
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Course findOne(@PathVariable Long id) {
        return courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);
    }

    @PostMapping
    public Course addNewCourse(@RequestBody Course course) {
        if (course.getId() != null)
            throw new ExistingEntityException();
        courseRepository.save(course);
        return course;
    }

    /**
     * Permanently delete a course from the system
     *
     * @param id the id of the course to delete
     * @param auth the Authorization header for the logged in user
     * @throws CourseNotFoundException if the course doesn't exist
     * @throws InvalidTokenException if the token is invalid
     * @throws AuthorizationException if the user doesn't have access
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id, @RequestHeader("Authorization") String auth) {
        Course course = courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        User user = tokenAuthService.getUserForToken(auth);

        CourseRole.CourseRoleType role = courseRoleService.getCourseRole(user, course);
        if (!role.equals(CourseRole.CourseRoleType.OWNER))
            throw new AuthorizationException();

        courseRepository.deleteById(id);
    }
}
