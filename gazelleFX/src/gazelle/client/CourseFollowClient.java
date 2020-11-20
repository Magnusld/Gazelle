package gazelle.client;

import gazelle.api.CourseResponse;
import gazelle.api.UserResponse;
import gazelle.api.ValueWrapper;
import gazelle.model.Course;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class CourseFollowClient {

    private final GazelleSession session;

    public CourseFollowClient(GazelleSession session) {
        this.session = session;
    }

    public List<CourseResponse> getFollowedCourses(Long userId) {
        WebTarget path = session.path("/users/{userId}/followedCourses")
                .resolveTemplate("userId", userId);
        return Requester.get(session.authorizedPath(path),
                new GenericType<List<CourseResponse>>() {});
    }

    public List<UserResponse> getCourseFollowers(Long courseId) {
        WebTarget path = session.path("/courses/{courseId}/followers")
                .resolveTemplate("courseId", courseId);
        return Requester.get(session.authorizedPath(path),
                new GenericType<List<UserResponse>>() {});
    }

    public void addCourseFollower(Long userId, ValueWrapper<Long> courseId) {
        WebTarget path = session.path("/users/{userId}/followedCourses")
                .resolveTemplate("userId", userId);
        Requester.post(
                session.authorizedPath(path), courseId);
    }

    public void removeCourseFollower(Long userId, Long courseId) {
        WebTarget path = session.path("/users/{userId}/followedCourses/{courseId}")
                .resolveTemplate("userId", userId)
                .resolveTemplate("courseId", courseId);
        Requester.delete(session.authorizedPath(path));
    }
}
