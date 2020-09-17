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

        assertSame(db.getCourse(c1.getId()), c1);
        assertSame(db.getUser(u1.getId()), u1);
        assertSame(db.getUser(u2.getId()), u2);
        assertNull(db.getUser(c1.getId()));

        assertEquals(db.getCourses().size(), 1);
        assertTrue(db.getCourses().contains(c1));
        assertEquals(db.getUsers().size(), 2);
        assertTrue(db.getUsers().contains(u1));
    }

    @Test
    public void databaseCrossTest() {
        Database db = new Database();
        Course c1 = db.newCourse("Hei");
        assertSame(c1.getDatabase(), db);

        Database db2 = new Database();
        User u1 = db2.newUser();

        assertThrows(IllegalArgumentException.class, ()->u1.addAsOwner(c1));
        assertThrows(IllegalArgumentException.class, ()->c1.addOwner(u1));
    }
}
