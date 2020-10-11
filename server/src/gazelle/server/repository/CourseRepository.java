package gazelle.server.repository;

import gazelle.model.Course;
import gazelle.model.CourseRole.CourseRoleType;
import gazelle.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends CrudRepository<Course, Long> {

    /**
     * Finds all courses where the specified user has the specified role
     *
     * <p>See: https://www.baeldung.com/spring-data-jpa-query
     * <br>See:https://www.baeldung.com/jpa-join-types
     * @param user the id of the user in question
     * @param role the type of role
     * @return all courses in which the user has the specified role
     */
    @Query("SELECT c FROM Course c JOIN c.roles r WHERE r.user = :user AND r.roleType = :role")
    List<Course> findAllByUserWithRole(
            @Param("user") User user,
            @Param("role") CourseRoleType role);

    /**
     * Finds all courses where the specified user has any role
     *
     * @param user the id of the user in question
     * @return all courses in which the user has any role
     */
    @Query("SELECT c FROM Course c JOIN c.roles r WHERE r.user = :user")
    List<Course> findAllByUser(@Param("user") User user);
}
