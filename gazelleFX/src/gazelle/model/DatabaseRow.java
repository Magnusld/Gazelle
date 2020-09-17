package gazelle.model;

public class DatabaseRow {
    private Database.Id id;

    public DatabaseRow(Database.Id id) {
        this.id = id;
    }

    public Long getId() {
        return id.getId();
    }

    public Database getDatabase() {
        return id.getDatabase();
    }
}
