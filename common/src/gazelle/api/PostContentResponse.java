package gazelle.api;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PostContentResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long courseId;
    private String courseName;
    @Nullable
    private Boolean isOwning;
    private List<ChoreResponse> chores;

    public static class Builder {
        private final PostContentResponse built;

        public Builder() {
            built = new PostContentResponse();
        }

        public PostContentResponse build() {
            built.validate();
            return built;
        }

        public Builder id(Long id) {
            built.setId(id);
            return this;
        }

        public Builder title(String title) {
            built.setTitle(title);
            return this;
        }

        public Builder description(String description) {
            built.setDescription(description);
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            built.setStartDate(startDate);
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            built.setEndDate(endDate);
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

        public Builder isOwning(@Nullable Boolean isOwning) {
            built.setIsOwning(isOwning);
            return this;
        }

        public Builder chores(List<ChoreResponse> chores) {
            built.setChores(chores);
            return this;
        }
    }

    protected PostContentResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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

    public @Nullable Boolean isOwning() {
        return isOwning;
    }

    public void setIsOwning(@Nullable Boolean isOwning) {
        this.isOwning = isOwning;
    }

    public List<ChoreResponse> getChores() {
        return chores;
    }

    public void setChores(List<ChoreResponse> chores) {
        this.chores = chores;
    }

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(description);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        Objects.requireNonNull(courseId);
        Objects.requireNonNull(courseName);
        Objects.requireNonNull(chores);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostContentResponse)) return false;
        PostContentResponse that = (PostContentResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(courseId, that.courseId)
                && Objects.equals(courseName, that.courseName)
                && Objects.equals(chores, that.chores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, startDate,
                endDate, courseId, courseName, chores);
    }
}
