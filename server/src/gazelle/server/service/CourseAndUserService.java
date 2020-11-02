package gazelle.server.service;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.endpoint.CourseController;
import gazelle.server.endpoint.UserController;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CourseAndUserService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public CourseAndUserService(UserRepository userRepository, CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * Add a user as an owner, unless it already owns the course.
     *
     * @param user the user
     * @param course the course
     */
    public void addOwner(User user, Course course) {
        user.getOwning().add(course);
        course.getOwners().add(user);
    }

    /**
     * Remove ownership of a course from a user.
     *
     * @param user the user
     * @param course the course
     * @return true if the course was previously owned by the user
     */
    public boolean removeOwner(User user, Course course) {
        final boolean result = user.getOwning().remove(course);
        course.getOwners().remove(user);
        return result;
    }

    /**
     * Checks if a use follows a course
     *
     * @param user the user
     * @param course the course
     * @return true if user follows the course
     */
    public boolean isOwning(User user, Course course) {
        return user.getOwning().contains(course);
    }

    @Transactional
    public boolean isOwning(Long userId, Long courseId) {
        return isOwning(
                userRepository.findById(userId).orElseThrow(UserNotFoundException::new),
                courseRepository.findById(courseId).orElseThrow(CourseNotFoundException::new));
    }


    /**
     * Add a user as a follower, unless it already owns the course.
     *
     * @param user the user
     * @param course the course
     */
    public void addFollower(User user, Course course) {
        user.getFollowing().add(course);
        course.getFollowers().add(user);
    }

    /**
     * Unfollow a user from a course.
     *
     * @param user the user
     * @param course the course
     * @return true if the course was previously followed by the user
     */
    public boolean removeFollower(User user, Course course) {
        final boolean result = user.getFollowing().remove(course);
        course.getFollowers().remove(user);
        return result;
    }

    /**
     * Checks if a use follows a course
     *
     * @param user the user
     * @param course the course
     * @return true if user follows the course
     */
    public boolean isFollowing(User user, Course course) {
        return user.getFollowing().contains(course);
    }
}
