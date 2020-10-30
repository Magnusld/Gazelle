package gazelle.server.service;

import gazelle.model.Course;
import gazelle.model.User;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseAndUserService {

    private final UserRepository userRepository;

    @Autowired
    public CourseAndUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Add a user as an owner, unless it already owns the course.
     *
     * <p><b>Important:</b> The User and Course objects must be fresh out of the database.
     * @param user the user
     * @param course the course
     */
    public void addOwner(User user, Course course) {
        user.getOwning().add(course);
        course.getOwners().add(user);
        userRepository.save(user);
    }

    /**
     * Remove ownership of a course from a user.
     *
     * <p><b>Important:</b> The User and Course objects must be fresh out of the database.
     * @param user the user
     * @param course the course
     * @return true if the course was previously owned by the user
     */
    public boolean removeOwner(User user, Course course) {
        boolean result = user.getOwning().remove(course);
        course.getOwners().remove(user);
        userRepository.save(user);
        return result;
    }

    /**
     * Add a user as a follower, unless it already owns the course.
     *
     * <p><b>Important:</b> The User and Course objects must be fresh out of the database.
     * @param user the user
     * @param course the course
     */
    public void addFollower(User user, Course course) {
        user.getFollowing().add(course);
        course.getFollowers().add(user);
        userRepository.save(user);
    }

    /**
     * Unfollow a user from a course.
     *
     * <p><b>Important:</b> The User and Course objects must be fresh out of the database.
     * @param user the user
     * @param course the course
     * @return true if the course was previously followed by the user
     */
    public boolean removeFollower(User user, Course course) {
        boolean result = user.getFollowing().remove(course);
        course.getFollowers().remove(user);
        userRepository.save(user);
        return result;
    }
}
