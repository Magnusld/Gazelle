package gazelle.server.endpoint;

import gazelle.api.ChoreResponse;
import gazelle.api.PostContentResponse;
import gazelle.api.PostResponse;
import gazelle.model.Chore;
import gazelle.model.Course;
import gazelle.model.Post;
import gazelle.model.User;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.PostNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.PostRepository;
import gazelle.server.service.TokenAuthService;
import gazelle.util.DateHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
public class PostController {

    private final PostRepository postRepository;
    private final TokenAuthService tokenAuthService;
    private final CourseRepository courseRepository;
    private final ChoreController choreController;

    @Autowired
    public PostController(PostRepository postRepository,
                          TokenAuthService tokenAuthService,
                          CourseRepository courseRepository,
                          ChoreController choreController) {
        this.postRepository = postRepository;
        this.tokenAuthService = tokenAuthService;
        this.courseRepository = courseRepository;
        this.choreController = choreController;
    }

    /**
     * Makes a serializable object with post metadata,
     * as well as info about relationships between the post and course / user.
     * Does not contain the full content of the post.
     * The user is optional. If not supplied, fields about progress are omitted.
     *
     * @param post the post
     * @param user an optional user object
     * @return PostResponse for the given post
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public PostResponse makePostResponse(Post post, @Nullable User user) {
        PostResponse.Builder builder = new PostResponse.Builder();
        builder.id(post.getId())
                .title(post.getTitle())
                .startDate(DateHelper.localDateOfDate(post.getStartDate()))
                .endDate(DateHelper.localDateOfDate(post.getEndDate()));

        Set<Chore> chores = post.getChores();
        builder.choresCount(chores.size());
        if (user != null) {
            int choresDone = 0;
            int choresFocused = 0;
            for (Chore chore : chores) {
                System.nanoTime(); //TODO: Add chore completion
            }
            builder.choresDone(choresDone).choresFocused(choresFocused);
        }
        return builder.build();
    }

    /**
     * Makes a serializable object containing the post content.
     * If a User is supplied, the ChoreResponses will contain the current progress for that user.
     *
     * @param post the post
     * @param user the user, or null
     * @return PostContentResponse for the given post
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public PostContentResponse makePostContentResponse(Post post, @Nullable User user) {
        PostContentResponse.Builder builder = new PostContentResponse.Builder();
        builder.id(post.getId());
        builder.title(post.getTitle());
        builder.description(post.getDescription());
        builder.startDate(DateHelper.localDateOfDate(post.getStartDate()));
        builder.startDate(DateHelper.localDateOfDate(post.getStartDate()));

        List<ChoreResponse> chores = new ArrayList<ChoreResponse>();
        for (Chore c : post.getChores())
            chores.add(choreController.makeChoreResponse(c, user));

        builder.chores(chores);

        return builder.build();
    }

    @GetMapping("/courses/{courseId}/posts")
    @Transactional
    public List<PostResponse> getPostsForCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader("Authorization") @Nullable String auth) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        Iterable<Post> posts = postRepository.findByCourseOrderByStartDateAsc(course);
        User user = null;
        if (auth != null)
            user = tokenAuthService.getUserObjectFromToken(auth);
        List<PostResponse> responses = new ArrayList<>();
        for (Post p : posts)
            responses.add(makePostResponse(p, user));
        return responses;
    }

    @GetMapping("/posts/{postId}")
    @Transactional
    public PostContentResponse getPostContent(
            @PathVariable("postId") Long postId,
            @RequestHeader("Authorization") @Nullable String auth) {
        User user = null;
        if (auth != null)
            user = tokenAuthService.getUserObjectFromToken(auth);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        return makePostContentResponse(post, user);
    }
}
