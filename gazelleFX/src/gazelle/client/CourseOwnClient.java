package gazelle.client;

import gazelle.api.CourseResponse;
import gazelle.api.UserResponse;
import gazelle.api.ValueWrapper;

import javax.ws.rs.client.WebTarget;
import java.util.List;

public class CourseOwnClient {

    private final GazelleSession session;

    public CourseOwnClient(GazelleSession session) {
        this.session = session;
    }

    public List<CourseResponse> getOwnedCourses(Long userId) {
        WebTarget path = session.path("/users/{userId}/ownedCourses")
                .resolveTemplate("userId", userId);
        return Requester.get(session.authorizedPath(path), List.class);
    }

    public List<UserResponse> getCourseOwners(Long courseId) {
        WebTarget path = session.path("/courses/{courseId}/owners")
                .resolveTemplate("courseId", courseId);
        return Requester.get(session.authorizedPath(path), List.class);
    }

    public void addCourseOwner(Long userId, ValueWrapper<Long> courseId) {
        WebTarget path = session.path("/users/{userId}/ownedCourses")
                .resolveTemplate("userId", userId);
        Requester.postWithResponse(session.authorizedPath(path), courseId, void.class);
    }

    public void removeCourseOwner(Long userId, Long courseId) {
        WebTarget path = session.path("/users/{userId}/ownedCourses/{courseId}")
                .resolveTemplate("userId", userId);
        Requester.delete(session.authorizedPath(path));
    }
}
