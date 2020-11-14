package gazelle.model;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class UserChoreProgress {
    public enum Progress {
        UNDONE("undone"),
        FOCUSED("focused"),
        DONE("done");

        private final String text;

        Progress(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }

        @JsonCreator
        public static Progress fromString(String text) {
            for (Progress p : Progress.values())
                if (p.text.equals(text))
                    return p;
            throw new IllegalArgumentException("Unknown text: " + text);
        }
    }

    @Embeddable
    public static class UserChoreKey implements Serializable {
        @Column(name = "user_id")
        Long userId;

        @Column(name = "chore_id")
        Long choreId;

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getChoreId() {
            return choreId;
        }

        public void setChoreId(Long choreId) {
            this.choreId = choreId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UserChoreKey)) return false;
            UserChoreKey that = (UserChoreKey) o;
            return Objects.equals(userId, that.userId)
                    && Objects.equals(choreId, that.choreId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(userId, choreId);
        }
    }

    @EmbeddedId
    private UserChoreKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("choreId")
    @JoinColumn(name = "chore_id")
    private Chore chore;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Progress progress;

    protected UserChoreProgress() {}

    public UserChoreProgress(User user, Chore chore, Progress progress) {
        this.user = user;
        this.chore = chore;
        this.progress = progress;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Chore getChore() {
        return chore;
    }

    public void setChore(Chore chore) {
        this.chore = chore;
    }

    public Progress getProgress() {
        return progress;
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserChoreProgress)) return false;
        UserChoreProgress that = (UserChoreProgress) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
