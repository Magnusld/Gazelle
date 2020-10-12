package gazelle.server.repository;

import gazelle.model.Course;
import gazelle.model.CourseRole;
import gazelle.model.CourseRoleKey;
import gazelle.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CourseRoleRepository extends CrudRepository<CourseRole, CourseRoleKey> {
    boolean existsByUserAndCourse(User user, Course course);

    Optional<CourseRole> findByUserAndCourse(User user, Course course);
}
