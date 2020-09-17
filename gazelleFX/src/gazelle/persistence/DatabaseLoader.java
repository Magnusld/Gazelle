package gazelle.persistence;

import gazelle.model.Database;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.Objects;

public class DatabaseLoader implements Closeable {

    private ObjectInputStream ois;
    public DatabaseLoader(InputStream is) throws IOException {
        Objects.requireNonNull(is);
        this.ois = new ObjectInputStream(is);
    }

    public void close() throws IOException {
        this.ois.close();
    }

    public Database load() throws IOException {
        Database database = new Database();
        database.load(ois);
        return database;
    }
}
