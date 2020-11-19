package gazelle.client;

import gazelle.api.CourseResponse;

import javax.ws.rs.client.WebTarget;
import java.util.List;

public class CourseClient {

    private final GazelleSession session;

    public CourseClient(GazelleSession session) {
        this.session = session;
    }

    public List<CourseResponse> getCoursesOwnedBy(Long userId) {
        WebTarget path = session.path("users/:userId/ownedCourses")
                .resolveTemplate("userId", userId);
        session.authorizedPath(path);
    }
}
