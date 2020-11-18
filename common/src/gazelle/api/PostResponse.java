package gazelle.api;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public class PostResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    @Nullable
    private ChoreResponse nextChoreDue;
    @Nullable
    private Integer choresDone;
    @Nullable
    private Integer choresFocused;
    private int choresCount;

    public static class Builder {
        private final PostResponse built;

        public Builder() {
            built = new PostResponse();
        }

        public PostResponse build() {
            built.validate();
            return built;
        }

        public Builder id(Long id) {
            built.setId(id);
            return this;
        }

        public Builder title(String name) {
            built.setTitle(name);
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

        public Builder nextChoreDue(ChoreResponse nextChoreDue) {
            built.setNextChoreDue(nextChoreDue);
            return this;
        }

        public Builder choresDone(@Nullable Integer choresDone) {
            built.setChoresDone(choresDone);
            return this;
        }

        public Builder choresFocused(@Nullable Integer choresFocused) {
            built.setChoresFocused(choresFocused);
            return this;
        }

        public Builder choresCount(int choresCount) {
            built.setChoresCount(choresCount);
            return this;
        }
    }

    protected PostResponse() {}

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

    public @Nullable ChoreResponse getNextChoreDue() {
        return nextChoreDue;
    }

    public void setNextChoreDue(@Nullable ChoreResponse nextChoreDue) {
        this.nextChoreDue = nextChoreDue;
    }

    public @Nullable Integer getChoresDone() {
        return choresDone;
    }

    public void setChoresDone(@Nullable Integer choresDone) {
        this.choresDone = choresDone;
    }

    public @Nullable Integer getChoresFocused() {
        return choresFocused;
    }

    public void setChoresFocused(@Nullable Integer choresFocused) {
        this.choresFocused = choresFocused;
    }

    public int getChoresCount() {
        return choresCount;
    }

    public void setChoresCount(int choresCount) {
        this.choresCount = choresCount;
    }

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostResponse)) return false;
        PostResponse that = (PostResponse) o;
        return choresCount == that.choresCount
                && Objects.equals(title, that.title)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(choresDone, that.choresDone)
                && Objects.equals(choresFocused, that.choresFocused);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, startDate, endDate, choresDone, choresFocused, choresCount);
    }
}
