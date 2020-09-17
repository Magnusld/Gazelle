package gazelle.model;

import java.io.Serializable;

public abstract class DatabaseRow implements Serializable {
    private transient Database.Id id;

    public DatabaseRow(Database.Id id) {
        this.id = id;
    }

    public Long getId() {
        return id.getId();
    }

    public Database getDatabase() {
        return id.getDatabase();
    }

    public void respecifyId(Database.Id id) {
        this.id = id;
    }
}
