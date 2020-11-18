package gazelle.api;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class CourseContentResponse {
    private Long id;
    private String name;
    @Nullable
    private Boolean isFollower;
    @Nullable
    private Boolean isOwner;
    private List<PostResponse> posts;

    public static class Builder {
        private final CourseContentResponse built;

        public Builder() {
            built = new CourseContentResponse();
        }

        public CourseContentResponse build() {
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

        public Builder isFollower(@Nullable Boolean isFollower) {
            built.setIsFollower(isFollower);
            return this;
        }

        public Builder isOwner(@Nullable Boolean isOwner) {
            built.setIsOwner(isOwner);
            return this;
        }

        public Builder posts(List<PostResponse> posts) {
            built.setPosts(posts);
            return this;
        }
    }

    protected CourseContentResponse() {}

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

    public @Nullable Boolean isFollower() {
        return isFollower;
    }

    public void setIsFollower(@Nullable Boolean isFollower) {
        this.isFollower = isFollower;
    }

    public @Nullable Boolean isOwner() {
        return isOwner;
    }

    public void setIsOwner(@Nullable Boolean isOwner) {
        this.isOwner = isOwner;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(name);
        Objects.requireNonNull(posts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseContentResponse)) return false;
        CourseContentResponse that = (CourseContentResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(isFollower, that.isFollower)
                && Objects.equals(isOwner, that.isOwner)
                && Objects.equals(posts, that.posts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isFollower, isOwner, posts);
    }
}
