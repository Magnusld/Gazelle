package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.GazelleException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @GetMapping
    public Iterable findAll() {
        return courseRepository.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Course> findByRoles_user(@PathVariable long userId) {
        return courseRepository.findByRoles_UserId(userId);
    }

    @GetMapping("/{id}")
    public Course findOne(@PathVariable Long id) {
        return unwrapOrThrow(courseRepository.findById(id));
    }

    @PostMapping
    public Course addCourse(@RequestBody Course course) {
        if (course.getId() != null && courseRepository.existsById(course.getId()))
            throw new GazelleException("Course id taken",
                    course.getId().toString(), HttpStatus.CONFLICT);
        courseRepository.save(course);
        return course;
    }

    static Course unwrapOrThrow(Optional<Course> course) {
        return course.orElseThrow(CourseNotFoundException::new);
    }
}
