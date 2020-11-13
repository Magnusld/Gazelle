package gazelle.server.endpoint;

import gazelle.api.PostResponse;
import gazelle.model.Chore;
import gazelle.model.Course;
import gazelle.model.Post;
import gazelle.model.User;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.PostRepository;
import gazelle.server.service.TokenAuthService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PostController(PostRepository postRepository,
                          TokenAuthService tokenAuthService,
                          CourseRepository courseRepository) {
        this.postRepository = postRepository;
        this.tokenAuthService = tokenAuthService;
        this.courseRepository = courseRepository;
    }

    /**
     * Makes a serializable object with post data, as well as info about relationships between the post and
     * @param post
     * @param user
     * @return
     */
    public PostResponse makePostResponse(Post post, @Nullable User user) {
        PostResponse.Builder builder = new PostResponse.Builder();
        builder.id(post.getId())
                .title(post.getTitle())
                .startDate(post.getStartDate())
                .endDate(post.getEndDate())
                .courseName(post.getCourse().getName())
                .courseId(post.getCourse().getId());

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

    @GetMapping("/courses/{courseId}/posts")
    @Transactional
    public List<PostResponse> getPostsForCourse(@PathVariable("courseId") Long courseId,
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
}
