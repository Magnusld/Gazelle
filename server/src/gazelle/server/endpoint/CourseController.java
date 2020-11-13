package gazelle.server.endpoint;

import gazelle.api.CourseResponse;
import gazelle.api.NewCourseRequest;
import gazelle.api.PostResponse;
import gazelle.model.Course;
import gazelle.model.ModelException;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.CourseNotFoundException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.UnprocessableEntityException;
import gazelle.server.repository.CourseRepository;
import gazelle.server.repository.PostRepository;
import gazelle.server.service.CourseAndUserService;
import gazelle.server.service.TokenAuthService;
import gazelle.util.DateHelper;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;
    private final TokenAuthService tokenAuthService;
    private final CourseAndUserService courseAndUserService;
    private final PostRepository postRepository;
    private final PostController postController;

    @Autowired
    public CourseController(CourseRepository courseRepository,
                            TokenAuthService tokenAuthService,
                            CourseAndUserService courseAndUserService,
                            PostRepository postRepository,
                            PostController postController) {
        this.courseRepository = courseRepository;
        this.tokenAuthService = tokenAuthService;
        this.courseAndUserService = courseAndUserService;
        this.postRepository = postRepository;
        this.postController = postController;
    }

    /**
     * Creates a serializable object with course data and other info related to the course.
     * If a user is provided, info about the relationship is also returned.
     * @param course the course object
     * @param user the user object, or null
     * @return CourseResponse object
     */
    public CourseResponse makeCourseResponse(Course course, @Nullable User user) {
        Boolean isOwner = null;
        Boolean isFollower = null;
        if (user != null) {
            isOwner = courseAndUserService.isOwning(user, course);
            isFollower = courseAndUserService.isFollowing(user, course);
        }

        Date today = DateHelper.today();

        PostResponse currentPost = postRepository.findCurrentPostInCourse(course, today)
                .map(it -> postController.makePostResponse(it, user))
                .orElse(null);
        PostResponse nextPost = postRepository.findNextPostInCourse(course, today)
                .map(it -> postController.makePostResponse(it, user))
                .orElse(null);

        return new CourseResponse(course.getId(), course.getName(), isOwner,
                isFollower, currentPost, nextPost);
    }

    /**
     * Gets a list of all courses.
     * Does not require the Authorization header.
     * If none is supplied, isOwning will be null for all courses,
     * and no chores will be completed.
     *
     * @param auth an optional token, or null
     * @return CourseResponse objects for every course
     */
    @GetMapping
    @Transactional
    public List<CourseResponse> findAll(@RequestHeader("Authorization") @Nullable String auth) {
        User user = null;
        if (auth != null)
            user = tokenAuthService.getUserObjectFromToken(auth);
        List<CourseResponse> result = new ArrayList<>();
        courseRepository.findAll().forEach(it -> result.add(makeCourseResponse(it, null)));
        return result;
    }

    /**
     * Gets a CourseResponse for a single course.
     * Optionally takes an Authorization token for a user.
     * If none is supplied, isOwning will be null,
     * and no chores will be completed.
     *
     * @param id of the course in question
     * @param auth an optional token, or null
     * @return CourseResponse for the course with the given id
     */
    @GetMapping("/{id}")
    @Transactional
    public CourseResponse findById(@PathVariable Long id,
                                   @RequestHeader("Authorization") @Nullable String auth) {
        Course course = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        User user = null;
        if (auth != null)
            user = tokenAuthService.getUserObjectFromToken(auth);
        return makeCourseResponse(course, user);
    }

    /**
     * Makes a new course, and makes the creating user the owner
     * @param newCourse the data for the new course
     * @param auth the token for the user
     * @return CourseResponse for the new course
     */
    @PostMapping
    @Transactional
    public CourseResponse addNewCourse(@RequestBody NewCourseRequest newCourse,
                                       @RequestHeader("Authorization") String auth) {
        User user = tokenAuthService.getUserObjectFromToken(auth);

        Course course = newCourse.buildCourse();
        try {
            course.validate();
        } catch (ModelException e) {
            throw new UnprocessableEntityException(e.getMessage());
        }

        courseRepository.save(course);
        courseAndUserService.addOwner(user, course);

        return makeCourseResponse(course, user);
    }

    /**
     * Permanently delete a course from the system
     *
     * @param id the id of the course to delete
     * @param auth the Authorization header for the logged in user
     * @throws CourseNotFoundException if the course doesn't exist
     * @throws InvalidTokenException if the token is invalid
     * @throws AuthorizationException if the user doesn't have access
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteCourse(@PathVariable Long id, @RequestHeader("Authorization") String auth) {
        Course course = courseRepository.findById(id)
                .orElseThrow(CourseNotFoundException::new);

        User user = tokenAuthService.getUserObjectFromToken(auth);

        if (!courseAndUserService.isOwning(user, course))
            throw new AuthorizationException();

        courseRepository.deleteById(id);
    }
}
