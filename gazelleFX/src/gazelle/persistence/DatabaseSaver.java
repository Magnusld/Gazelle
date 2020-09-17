package gazelle.persistence;

import gazelle.model.Database;

import java.io.*;
import java.util.Objects;

public class DatabaseSaver implements Closeable {

    private ObjectOutputStream oos;

    public DatabaseSaver(OutputStream os) throws IOException {
        Objects.requireNonNull(os);
        this.oos = new ObjectOutputStream(os);
    }

    public void close() throws IOException {
        this.oos.close();
    }

    public void save(Database database) throws IOException {
        database.dump(oos);
        oos.flush();
    }
}
