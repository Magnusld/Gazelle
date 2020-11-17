package gazelle.server.repository;

import gazelle.model.Chore;
import gazelle.model.Course;
import gazelle.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.Optional;

public interface ChoreRepository extends CrudRepository<Chore, Long> {

    /**
     * Gets the next chore with a deadline in the course, defined by:
     *  - It has a due date >= today
     *  - It has the earliest due date
     * @param course the course
     * @param date today
     * @return the next Chore with a deadline, today or later
     */
    @Query("SELECT c FROM Chore c WHERE c.post.course = :course AND "
            + "c.dueDate IS NOT NULL AND c.dueDate >= :date ORDER BY d.dueDate ASC")
    Optional<Chore> findNextDueDateInCourse(@Param("course") Course course,
                                            @Param("date") Date date);

    /**
     * Gets the next chore with a deadline in the post, defined by:
     *  - It has a due date >= today
     *  - It has the earliest due date
     * @param post the post
     * @param date today
     * @return the next Chore with a deadline, today or later
     */
    @Query("SELECT c FROM Chore c WHERE c.post = :post AND "
            + "c.dueDate IS NOT NULL AND c.dueDate >= :date ORDER BY c.dueDate ASC")
    Optional<Chore> findNextDueDateInPost(@Param("post") Post post,
                                            @Param("date") Date date);
}
