package gazelle.server.repository;

import gazelle.model.Course;
import gazelle.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface CourseRepository extends CrudRepository<Course, Long> {
    
}
