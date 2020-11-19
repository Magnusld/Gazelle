package gazelle.api;

import gazelle.model.UserChoreProgress;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public class ChoreFullResponse {
    private Long id;
    private Long key;
    private String text;
    @Nullable
    private LocalDate dueDate;
    @Nullable
    private UserChoreProgress.Progress progress;
    private Long postId;
    private String postTitle;
    private Long courseId;
    private String courseName;

    public static class Builder {
        private final ChoreFullResponse built;

        public Builder() {
            built = new ChoreFullResponse();
        }

        public ChoreFullResponse build() {
            built.validate();
            return built;
        }

        public Builder id(Long id) {
            built.setId(id);
            return this;
        }

        public Builder key(Long key) {
            built.setKey(key);
            return this;
        }

        public Builder text(String text) {
            built.setText(text);
            return this;
        }

        public Builder dueDate(@Nullable LocalDate dueDate) {
            built.setDueDate(dueDate);
            return this;
        }

        public Builder progress(@Nullable UserChoreProgress.Progress progress) {
            built.setProgress(progress);
            return this;
        }

        public Builder postId(Long postId) {
            built.setPostId(postId);
            return this;
        }

        public Builder postTitle(String postTitle) {
            built.setPostTitle(postTitle);
            return this;
        }

        public Builder courseId(Long courseId) {
            built.setCourseId(courseId);
            return this;
        }

        public Builder courseName(String courseName) {
            built.setCourseName(courseName);
            return this;
        }
    }

    protected ChoreFullResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public @Nullable LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@Nullable LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public @Nullable UserChoreProgress.Progress getProgress() {
        return progress;
    }

    public void setProgress(@Nullable UserChoreProgress.Progress progress) {
        this.progress = progress;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(key);
        Objects.requireNonNull(text);
        Objects.requireNonNull(postId);
        Objects.requireNonNull(postTitle);
        Objects.requireNonNull(courseId);
        Objects.requireNonNull(courseName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoreFullResponse)) return false;
        ChoreFullResponse that = (ChoreFullResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(key, that.key)
                && Objects.equals(text, that.text)
                && Objects.equals(dueDate, that.dueDate)
                && progress == that.progress
                && Objects.equals(postId, that.postId)
                && Objects.equals(postTitle, that.postTitle)
                && Objects.equals(courseId, that.courseId)
                && Objects.equals(courseName, that.courseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, text, dueDate, progress,
                postId, postTitle, courseId, courseName);
    }
}
