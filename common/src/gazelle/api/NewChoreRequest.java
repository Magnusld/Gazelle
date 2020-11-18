package gazelle.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.util.Objects;

public class NewChoreRequest {
    /**
     * id is only provided if we are overriding an existing Chore
     */
    @Nullable
    private Long id;
    /**
     * key is used when ordering the chores on the front end
     */
    private Long key;
    private String text;
    @Nullable
    private LocalDate dueDate;

    protected NewChoreRequest() {}

    public NewChoreRequest(@Nullable Long id, Long key, String text, @Nullable LocalDate dueDate) {
        this.id = id;
        this.key = key;
        this.text = text;
        this.dueDate = dueDate;
    }

    public @Nullable Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
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

    public void validate() {
        Objects.requireNonNull(key);
        Objects.requireNonNull(text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewChoreRequest)) return false;
        NewChoreRequest that = (NewChoreRequest) o;
        return Objects.equals(id, that.id)
                && Objects.equals(key, that.key)
                && Objects.equals(text, that.text)
                && Objects.equals(dueDate, that.dueDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key, text, dueDate);
    }
}
