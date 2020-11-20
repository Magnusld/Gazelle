package gazelle.client;

import gazelle.api.NewPostRequest;
import gazelle.api.PostContentResponse;

import javax.ws.rs.client.WebTarget;

public class PostClient {

    private final GazelleSession session;

    public PostClient(GazelleSession session) {
        this.session = session;
    }

    public PostContentResponse getPostContent(Long postId) {
        WebTarget path = session.path("/posts/{postId}").resolveTemplate("postId", postId);
        return Requester.get(session.authorizedPath(path), PostContentResponse.class);
    }

    public PostContentResponse addNewPost(Long courseId, NewPostRequest newPostRequest) {
        WebTarget path = session.path("/courses/{courseId}/posts")
                .resolveTemplate("courseId", courseId);
        return Requester.postWithResponse(
                session.authorizedPath(path), newPostRequest, PostContentResponse.class);
    }

    public void updateExistingPost(Long postId, NewPostRequest newPostRequest) {
        WebTarget path = session.path("/posts/{postId}")
                .resolveTemplate("postId", postId);
        Requester.put(session.authorizedPath(path), newPostRequest);
    }

    public void deletePost(Long postId) {
        WebTarget path = session.path("/posts/{postId}")
                .resolveTemplate("postId", postId);
        Requester.delete(session.authorizedPath(path));
    }
}
