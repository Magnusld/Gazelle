package gazelle.server.repository;


import gazelle.model.Course;
import gazelle.model.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface PostRepository extends CrudRepository<Post, Long> {

    Iterable<Post> findByCourseOrderByStartDateAsc(Course course);

    /**
     * Find the current post in the course, based on:
     *  - it has started
     *  - it has not yet ended
     *  - if multiple posts fit, choose the one that most recently started
     *
     * @param course the course
     * @param date today
     * @return the current post, if any
     */
    @Query("SELECT p FROM Post p WHERE p.course = :course AND "
            + "p.startDate <= :date AND p.endDate >= :date ORDER BY p.startDate DESC")
    Iterable<Post> findCurrentPostInCourse(@Param("course") Course course,
                                           @Param("date") Date date);

    /**
     * Find the next post in the course, defined by
     *  - it has not yet started
     *  - it is the next post to start
     * @param course the course
     * @param date today
     * @return the first post to start after today
     */
    @Query("SELECT p FROM Post p WHERE p.course = :course AND "
            + "p.startDate > :date ORDER BY p.startDate ASC")
    Iterable<Post> findNextPostInCourse(@Param("course") Course course,
                                           @Param("date") Date date);

    /**
     * Find the previous post in a course, defined by
     *  - it has ended
     *  - it was the previous post to end
     * @param course the course
     * @param date today
     * @return the previous post in the course
     */
    @Query("SELECT p FROM Post p WHERE p.course = :course AND "
            + "p.endDate < :date ORDER BY p.endDate DESC")
    Iterable<Post> findPreviousPostInCourse(@Param("course") Course course,
                                            @Param("date") Date date);
}
