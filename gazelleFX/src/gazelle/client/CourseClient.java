package gazelle.client;

import gazelle.api.CourseContentResponse;
import gazelle.api.CourseResponse;
import gazelle.api.NewCourseRequest;
import gazelle.api.ValueWrapper;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class CourseClient {

    private final GazelleSession session;

    public CourseClient(GazelleSession session) {
        this.session = session;
    }

    public List<CourseResponse> findAll() {
        WebTarget path = session.path("courses");
        return Requester.get(session.authorizedPath(path),
                new GenericType<List<CourseResponse>>(){});
    }

    public CourseContentResponse findById(Long courseId) {
        WebTarget path = session.path("courses/{courseId}")
                .resolveTemplate("courseId", courseId);
        return Requester.get(session.authorizedPath(path),
                CourseContentResponse.class);
    }

    public CourseResponse addNewCourse(NewCourseRequest newCourse) {
        WebTarget path = session.path("courses");
        return Requester.postWithResponse(session.authorizedPath(path),
                newCourse, CourseResponse.class);
    }

    public void deleteCourse(Long courseId) {
        WebTarget path = session.path("courses/{courseId}")
                .resolveTemplate("courseId", courseId);
        Requester.delete(session.authorizedPath(path));
    }

    public List<CourseResponse> getFollowedCourses(Long userId) {
        WebTarget path = session.path("users/{userId}/followedCourses")
                .resolveTemplate("userId", userId);
        return Requester.get(session.authorizedPath(path),
                new GenericType<List<CourseResponse>>(){});
    }

    public void addCourseFollower(Long userId, Long courseId) {
        WebTarget path = session.path("users/{userId}/followedCourses")
                .resolveTemplate("userId", userId)
                .resolveTemplate("courseId", courseId);
        Requester.post(session.authorizedPath(path), new ValueWrapper<>(courseId));
    }

    public void removeCourseFollower(Long userId, Long courseId) {
        WebTarget path = session.path("users/{userId}/followedCourses/{courseId}")
                .resolveTemplate("userId", userId)
                .resolveTemplate("courseId", courseId);
        Requester.delete(session.authorizedPath(path));
    }

    public List<CourseResponse> getOwnedCourses(Long userId) {
        WebTarget path = session.path("users/{userId}/ownedCourses")
                .resolveTemplate("userId", userId);
        return Requester.get(session.authorizedPath(path),
                new GenericType<List<CourseResponse>>(){});
    }

}
