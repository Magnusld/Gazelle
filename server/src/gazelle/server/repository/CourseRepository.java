package gazelle.server.repository;

import gazelle.model.Course;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {

    Optional<Course> findById(long id);

    List<Course> findByRoles_UserId(long userId);
}