package gazelle.api;

import java.time.LocalDate;
import java.util.*;

public class NewPostRequest {
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<NewChoreRequest> chores;

    public static class Builder {
        private final NewPostRequest built;

        public Builder() {
            built = new NewPostRequest();
        }

        public NewPostRequest build() {
            built.validate();
            return built;
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

        public Builder chores(List<NewChoreRequest> chores) {
            built.setChores(chores);
            return this;
        }
    }

    protected NewPostRequest() {}

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

    public List<NewChoreRequest> getChores() {
        return chores;
    }

    public void setChores(List<NewChoreRequest> chores) {
        this.chores = chores;
    }

    public void validate() {
        Objects.requireNonNull(title);
        Objects.requireNonNull(description);
        Objects.requireNonNull(startDate);
        Objects.requireNonNull(endDate);
        Objects.requireNonNull(chores);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewPostRequest)) return false;
        NewPostRequest that = (NewPostRequest) o;
        return Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(startDate, that.startDate)
                && Objects.equals(endDate, that.endDate)
                && Objects.equals(chores, that.chores);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, startDate, endDate, chores);
    }
}
