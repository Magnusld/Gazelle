package gazelle.server.endpoint;

import gazelle.model.Course;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.ExistingEntityException;
import gazelle.server.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    @Autowired
    public CourseController(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
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
}
