package gazelle.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseResponse {
    private Long id;
    private String name;
    @Nullable
    private Boolean isOwner;
    @Nullable
    private Boolean isFollower;
    @JsonIgnoreProperties({ "courseName", "courseId" })
    @Nullable
    private PostResponse currentPost;
    @JsonIgnoreProperties({ "courseName", "courseId" })
    @Nullable
    private PostResponse nextPost;

    protected CourseResponse() {}

    public CourseResponse(Long id, String name,
                          @Nullable Boolean isOwner, @Nullable Boolean isFollower,
                          @Nullable PostResponse currentPost, @Nullable PostResponse nextPost) {
        this.id = id;
        this.name = name;
        this.isOwner = isOwner;
        this.isFollower = isFollower;
        this.currentPost = currentPost;
        this.nextPost = nextPost;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public @Nullable Boolean isOwner() {
        return isOwner;
    }

    public void setIsOwner(@Nullable Boolean owner) {
        isOwner = owner;
    }

    public @Nullable Boolean isFollower() {
        return isFollower;
    }

    public void setIsFollower(@Nullable Boolean follower) {
        isFollower = follower;
    }

    public @Nullable PostResponse getCurrentPost() {
        return currentPost;
    }

    public void setCurrentPost(@Nullable PostResponse currentPost) {
        this.currentPost = currentPost;
    }

    public @Nullable PostResponse getNextPost() {
        return nextPost;
    }

    public void setNextPost(@Nullable PostResponse nextPost) {
        this.nextPost = nextPost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseResponse)) return false;
        CourseResponse that = (CourseResponse) o;
        return isOwner == that.isOwner
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(currentPost, that.currentPost)
                && Objects.equals(nextPost, that.nextPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isOwner, currentPost, nextPost);
    }
}
