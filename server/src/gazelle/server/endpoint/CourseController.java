package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.ExistingEntityException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.service.CourseAndUserService;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final TokenAuthService tokenAuthService;
    private final CourseAndUserService courseAndUserService;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            TokenAuthService tokenAuthService,
                            CourseAndUserService courseAndUserService) {
        this.courseRepository = courseRepository;
        this.tokenAuthService = tokenAuthService;
        this.courseAndUserService = courseAndUserService;
    }

    @GetMapping
    public Iterable<Course> findAll() {
        return courseRepository.findAll();
    }

    @GetMapping("/{id}")
    public Course findById(@PathVariable Long id) {
        return courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);
    }

    @PostMapping
    @Transactional
    public Course addNewCourse(@RequestBody Course course,
                               @RequestHeader("Authorization") String auth) {
        if (course.getId() != null)
            throw new ExistingEntityException();

        User user = tokenAuthService.getUserObjectFromToken(auth);

        courseRepository.save(course);
        courseAndUserService.addOwner(user, course);

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
    @Transactional
    public void deleteCourse(@PathVariable Long id, @RequestHeader("Authorization") String auth) {
        Course course = courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        User user = tokenAuthService.getUserObjectFromToken(auth);

        if (!courseAndUserService.isOwning(user, course))
            throw new AuthorizationException();

        courseRepository.deleteById(id);
    }
}
