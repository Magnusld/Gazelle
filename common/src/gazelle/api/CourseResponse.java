package gazelle.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import gazelle.model.Course;
import gazelle.model.Post;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class CourseResponse {
    private Long id;
    private String name;
    @Nullable
    private Boolean isOwner;
    @Nullable
    private Boolean isFollower;
    @Nullable
    private PostResponse currentPost;
    @Nullable
    private PostResponse nextPost;
    @Nullable
    private PostResponse previousPost;
    @Nullable
    private ChoreResponse nextChoreDue;

    public static class Builder {
        private final CourseResponse built;

        public Builder() {
            built = new CourseResponse();
        }

        public CourseResponse build() {
            built.validate();
            return built;
        }

        public Builder id(Long id) {
            built.setId(id);
            return this;
        }

        public Builder name(String name) {
            built.setName(name);
            return this;
        }

        public Builder isOwner(@Nullable Boolean owner) {
            built.setIsOwner(owner);
            return this;
        }

        public Builder isFollower(@Nullable Boolean follower) {
            built.setIsFollower(follower);
            return this;
        }

        public Builder currentPost(@Nullable PostResponse currentPost) {
            built.setCurrentPost(currentPost);
            return this;
        }

        public Builder nextPost(@Nullable PostResponse nextPost) {
            built.setNextPost(nextPost);
            return this;
        }

        public Builder previousPost(@Nullable PostResponse nextPost) {
            built.setPreviousPost(nextPost);
            return this;
        }

        public Builder nextChoreDue(@Nullable ChoreResponse nextChoreDue) {
            built.setNextChoreDue(nextChoreDue);
            return this;
        }
    }

    protected CourseResponse() {}

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

    public @Nullable PostResponse getPreviousPost() {
        return previousPost;
    }

    public void setPreviousPost(@Nullable PostResponse previousPost) {
        this.previousPost = previousPost;
    }

    public @Nullable ChoreResponse getNextChoreDue() {
        return nextChoreDue;
    }

    public void setNextChoreDue(@Nullable ChoreResponse nextChoreDue) {
        this.nextChoreDue = nextChoreDue;
    }

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseResponse)) return false;
        CourseResponse that = (CourseResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(isOwner, that.isOwner)
                && Objects.equals(isFollower, that.isFollower)
                && Objects.equals(currentPost, that.currentPost)
                && Objects.equals(nextPost, that.nextPost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isOwner, currentPost, nextPost);
    }
}
