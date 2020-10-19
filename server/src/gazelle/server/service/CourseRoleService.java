package gazelle.server.service;

import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.User;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.CourseRoleRepository;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CourseRoleService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CourseRoleRepository courseRoleRepository;

    @Autowired
    public CourseRoleService(UserRepository userRepository,
                                CourseRepository courseRepository,
                                CourseRoleRepository courseRoleRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.courseRoleRepository = courseRoleRepository;
    }

    /**
     * Gets the role of a user in the specified course, or null if no role exists
     *
     * @param user the user
     * @param course the course
     * @return the role, or null if there is no relation
     */
    public CourseRole.CourseRoleType getCourseRole(User user, Course course) {
        return courseRoleRepository
                .findByUserAndCourse(user, course)
                .map(CourseRole::getRoleType)
                .orElse(null);
    }

    /**
     * Gets all courses where the user has the supplied role, or any role if null
     *
     * @param user the user
     * @param role the role we filter by (or no filter if null)
     * @return a list of all courses where the user has the role (or any role if null)
     */
    public List<Course> getCoursesForUser(User user, CourseRole.CourseRoleType role) {
        if (role == null)
            return courseRepository.findAllByUser(user);
        else
            return courseRepository.findAllByUserWithRole(user, role);
    }

    /**
     * Sets the role for the specified user on the specified course
     *
     * @param user the user
     * @param course the course
     * @param type the role type
     * @return true if we replaced an existing role
     */
    public boolean setRole(User user, Course course, CourseRole.CourseRoleType type) {
        Objects.requireNonNull(type);
        boolean existed = courseRoleRepository.existsByUserAndCourse(user, course);
        CourseRole role = new CourseRole(user, course, type);
        courseRoleRepository.save(role);
        return existed;
    }
}
