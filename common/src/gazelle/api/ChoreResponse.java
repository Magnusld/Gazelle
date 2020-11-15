package gazelle.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import gazelle.model.UserChoreProgress;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChoreResponse {
    private Long id;
    private Long key;
    private String text;
    @Nullable
    private LocalDate dueDate;
    @Nullable
    private UserChoreProgress.Progress progress;

    public static class Builder {
        private final ChoreResponse built;

        public Builder() {
            built = new ChoreResponse();
        }

        public ChoreResponse build() {
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

        public Builder dueDate(LocalDate dueDate) {
            built.setDueDate(dueDate);
            return this;
        }

        public Builder progress(UserChoreProgress.Progress progress) {
            built.setProgress(progress);
            return this;
        }
    }

    protected ChoreResponse() {}

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

    public void validate() {
        Objects.requireNonNull(id);
        Objects.requireNonNull(key);
        Objects.requireNonNull(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChoreResponse)) return false;
        ChoreResponse that = (ChoreResponse) o;
        return Objects.equals(text, that.text)
                && Objects.equals(dueDate, that.dueDate)
                && Objects.equals(progress, that.progress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, dueDate, progress);
    }
}
