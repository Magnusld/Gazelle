package gazelle.server.repository;

import gazelle.model.CourseRole;
import gazelle.model.CourseRoleKey;
import org.springframework.data.repository.CrudRepository;

public interface CourseRoleRepository extends CrudRepository<CourseRole, CourseRoleKey> {

}
