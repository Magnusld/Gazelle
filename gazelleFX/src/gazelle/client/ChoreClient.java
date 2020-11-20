package gazelle.client;

import gazelle.api.ChoreFullResponse;
import gazelle.api.UserResponse;
import gazelle.api.ValueWrapper;
import gazelle.model.UserChoreProgress;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

public class ChoreClient {

    private final GazelleSession session;

    public ChoreClient(GazelleSession session) {
        this.session = session;
    }

    public void setChoreState(Long choreId, Long userId, ValueWrapper<UserChoreProgress.Progress> value) {
        WebTarget path = session.path("/users/{userId}/chores/{choreId}/progress")
                .resolveTemplate("choreId", choreId).resolveTemplate("userId", userId);
        Requester.put(session.authorizedPath(path), value);
    }

    public List<ChoreFullResponse> getFocusedChores(Long userId) {
        WebTarget path = session.path("/users/{userId}/focusedChores/").resolveTemplate("userId", userId);
        return Requester.get(session.authorizedPath(path), new GenericType<List<ChoreFullResponse>>() {});
    }
}
