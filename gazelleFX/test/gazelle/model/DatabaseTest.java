package gazelle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {

    @Test
    public void databaseIdTest() {
        Database db = new Database();
        Course c1 = db.newCourse("Hei");
        User u1 = db.newUser();
        User u2 = db.newUser();

        assertTrue(db.getCourse(c1.getId()) == c1);
        assertTrue(db.getUser(u1.getId()) == u1);
        assertTrue(db.getUser(u2.getId()) == u2);
        assertTrue(db.getUser(c1.getId()) == null);
    }

    @Test
    public void databaseCrossTest() {
        Database db = new Database();
        Course c1 = db.newCourse("Hei");
        assertTrue(c1.getDatabase() == db);

        Database db2 = new Database();
        User u1 = db2.newUser();

        assertThrows(IllegalArgumentException.class, ()->u1.addAsOwner(c1));
        assertThrows(IllegalArgumentException.class, ()->c1.addOwner(u1));
    }
}
