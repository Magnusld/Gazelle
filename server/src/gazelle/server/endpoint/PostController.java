package gazelle.server.endpoint;

import gazelle.api.*;
import gazelle.model.*;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.PostNotFoundException;
import gazelle.server.error.UnprocessableEntityException;
import gazelle.server.repository.ChoreRepository;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.PostRepository;
import gazelle.server.repository.UserChoreProgressRepository;
import gazelle.server.service.ChoreProgressService;
import gazelle.server.service.CourseAndUserService;
import gazelle.server.service.TokenAuthService;
import gazelle.util.DateHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PostController {

    private final PostRepository postRepository;
    private final TokenAuthService tokenAuthService;
    private final CourseRepository courseRepository;
    private final ChoreController choreController;
    private final ChoreRepository choreRepository;
    private final CourseAndUserService courseAndUserService;
    private final ChoreProgressService choreProgressService;

    @Autowired
    public PostController(PostRepository postRepository,
                          TokenAuthService tokenAuthService,
                          CourseRepository courseRepository,
                          ChoreController choreController,
                          ChoreRepository choreRepository,
                          CourseAndUserService courseAndUserService,
                          ChoreProgressService choreProgressService) {
        this.postRepository = postRepository;
        this.tokenAuthService = tokenAuthService;
        this.courseRepository = courseRepository;
        this.choreController = choreController;
        this.choreRepository = choreRepository;
        this.courseAndUserService = courseAndUserService;
        this.choreProgressService = choreProgressService;
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
                .description(post.getDescription())
                .startDate(DateHelper.localDateOfDate(post.getStartDate()))
                .endDate(DateHelper.localDateOfDate(post.getEndDate()));

        Date today = DateHelper.today();

        builder.nextChoreDue(choreRepository.findNextDueDateInPost(post, today)
                .map(it -> choreController.makeChoreResponse(it, user))
                .orElse(null));

        Set<Chore> chores = post.getChores();
        builder.choresCount(chores.size());
        if (user != null) {
            int choresDone = 0;
            int choresFocused = 0;
            for (Chore chore : chores) {
                UserChoreProgress.Progress progress = choreProgressService.getProgress(user, chore);
                if (progress == UserChoreProgress.Progress.DONE)
                    choresDone++;
                else if (progress == UserChoreProgress.Progress.FOCUSED)
                    choresFocused++;
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
        builder.endDate(DateHelper.localDateOfDate(post.getEndDate()));
        builder.courseId(post.getCourse().getId());
        builder.courseName(post.getCourse().getName());

        List<ChoreResponse> chores = new ArrayList<>();
        for (Chore c : post.getChores())
            chores.add(choreController.makeChoreResponse(c, user));

        builder.chores(chores);

        return builder.build();
    }

    /**
     * Make a new Post object from the NewPostRequest in the specified course.
     * Creates chore objects for all new chores.
     * Adds the post to the course, which will persist it upon commit.
     *
     * @param r the NewPostRequest
     * @param course the Course the Post belongs to.
     * @return Post the new Post object
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public Post buildPost(NewPostRequest r, Course course) {
        r.validate();
        Post post = new Post(r.getTitle(), r.getDescription(), course,
                DateHelper.dateOfLocalDate(r.getStartDate()),
                DateHelper.dateOfLocalDate(r.getEndDate()));
        Set<Chore> choreList = new HashSet<>();
        for (NewChoreRequest c : r.getChores()) {
            choreList.add(choreController.buildChore(c, post));
        }
        post.setChores(choreList);

        course.getPosts().add(post);
        return post;
    }

    /**
     * Updates an existing Post with data from a NewPostRequest.
     * Any new Chores will be created, and existing Chores will be updated (based on id).
     * If any chores have been removed, the Chore object is deleted from the database.
     *
     * @param r the NewChoreRequest
     * @param post the Post to update
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public void updatePost(NewPostRequest r, Post post) {
        r.validate();
        post.setTitle(r.getTitle());
        post.setDescription(r.getDescription());
        Set<Chore> choreList = new HashSet<>();

        // Go through all chores we want to have after the update
        for (NewChoreRequest c : r.getChores()) {
            Long id = c.getId();
            if (id == null) // If the chore is completely new
                choreList.add(choreController.buildChore(c, post));
            else { // If the chore is an update of an existing chore
                Optional<Chore> updatedChoreOpt = post.getChores().stream()
                        .filter(it -> id.equals(it.getId())).findFirst();
                // Make sure the Chore actually belongs to this post
                Chore updatedChore = updatedChoreOpt.orElseThrow(() ->
                        new UnprocessableEntityException("The post doesn't contain chore " + id));
                choreController.updateChore(c, updatedChore);
                choreList.add(updatedChore);
            }
        }

        // Remove chores that are no longer part of the post
        for (Chore oldChore : post.getChores()) {
            if (choreList.contains(oldChore))
                continue;
            choreRepository.delete(oldChore);
        }
        post.setChores(choreList);
    }

    @GetMapping("/courses/{courseId}/posts")
    @Transactional
    public List<PostResponse> getPostsForCourse(
            @PathVariable("courseId") Long courseId,
            @RequestHeader(name = "Authorization", required = false) @Nullable String auth) {
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
            @RequestHeader(name = "Authorization", required = false) @Nullable String auth) {
        User user = null;
        if (auth != null)
            user = tokenAuthService.getUserObjectFromToken(auth);

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        return makePostContentResponse(post, user);
    }

    @PostMapping("/courses/{courseId}/posts")
    @Transactional
    public PostContentResponse addNewPost(
            @PathVariable("courseId") Long courseId,
            @RequestBody NewPostRequest request,
            @RequestHeader(name = "Authorization", required = false) @Nullable String auth) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(CourseNotFoundException::new);
        User user = tokenAuthService.getUserObjectFromToken(auth);
        if (!courseAndUserService.isOwning(user, course))
            throw new AuthorizationException("You don't own this course");

        Post post = buildPost(request, course);
        postRepository.save(post);

        return makePostContentResponse(post, user);
    }

    @PutMapping("/posts/{postId}")
    @Transactional
    public PostContentResponse updateExistingPost(
            @PathVariable("postId") Long postId,
            @RequestBody NewPostRequest request,
            @RequestHeader(name = "Authorization", required = false) @Nullable String auth) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        Course course = post.getCourse();
        User user = tokenAuthService.getUserObjectFromToken(auth);
        if (!courseAndUserService.isOwning(user, course))
            throw new AuthorizationException("You don't own this course");

        updatePost(request, post);
        return makePostContentResponse(post, user);
    }
}
