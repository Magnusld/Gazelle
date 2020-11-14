package gazelle.api;

import gazelle.model.UserChoreProgress;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public class ChoreResponse {
    private String text;
    @Nullable
    private LocalDate dueDate;
    @Nullable
    private UserChoreProgress.Progress progress;

    public ChoreResponse(String text, @Nullable LocalDate dueDate,
                         @Nullable UserChoreProgress.Progress progress) {
        this.text = text;
        this.dueDate = dueDate;
        this.progress = progress;
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
