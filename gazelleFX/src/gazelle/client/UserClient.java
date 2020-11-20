package gazelle.client;

import gazelle.api.UserResponse;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class UserClient {

    private final GazelleSession session;

    public UserClient(GazelleSession session) {
        this.session = session;
    }

    public List<UserResponse> findAll() {
        WebTarget path = session.path("/users");
        return Requester.get(session.authorizedPath(path),
                new GenericType<List<UserResponse>>() {});
    }

    public UserResponse findById(Long id) {
        WebTarget path = session.path("/users/{id}").resolveTemplate("id", id);
        return Requester.get(session.authorizedPath(path), UserResponse.class);
    }

    public void deleteUser(Long id) {
        WebTarget path = session.path("/users/{id}").resolveTemplate("id", id);
        Requester.delete(session.authorizedPath(path));

    }
}
