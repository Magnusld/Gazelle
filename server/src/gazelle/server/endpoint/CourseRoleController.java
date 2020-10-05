package gazelle.server.endpoint;

import gazelle.model.CourseRole;
import gazelle.server.repository.CourseRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courseRoles")
public class CourseRoleController {
    private final CourseRoleRepository courseRoleRepository;

    @Autowired
    public CourseRoleController(CourseRoleRepository courseRoleRepository) {
        this.courseRoleRepository = courseRoleRepository;
    }

    @PostMapping
    public CourseRole addCourseRole(@RequestBody CourseRole courseRole) {
        //TODO: Make sure user has permission
        courseRoleRepository.save(courseRole);
        return courseRole;
    }
}

