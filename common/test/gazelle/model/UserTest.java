package gazelle.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    public void validationTest() {
        User user = new User("Per", "Jensen", "per.jensen@hotmail.org", "123");

        ModelException ex = assertThrows(ModelException.class, user::validate);
        assertEquals("Passordet må være minst 4 tegn.", ex.getMessage());

        user.setPassword("5784");
        user.setFirstname(" Per");
        ex = assertThrows(ModelException.class, user::validate);
        assertEquals("Fornavn kan ikke starte eller slutte med mellomrom.", ex.getMessage());

        user.setFirstname("Per Jhonny");
        user.setLastname("Jensen ");
        ex = assertThrows(ModelException.class, user::validate);
        assertEquals("Etternavn kan ikke starte eller slutte med mellomrom.", ex.getMessage());

        user.setLastname("Jensen");
        user.setEmail("per. jensen@hotmail.org");
        ex = assertThrows(ModelException.class, user::validate);
        assertEquals("E-post-adressen kan ikke inneholde mellomrom.", ex.getMessage());

        user.setEmail("per.jensen@hotmail.org");
        user.setFirstname("Å");
        ex = assertThrows(ModelException.class, user::validate);
        assertEquals("Fornavn må være minst 2 tegn.", ex.getMessage());

        user.setFirstname("Per Jhonny");
        assertDoesNotThrow(user::validate);
    }

    @Test
    public void testEquals() {
        User user1 = new User("Per", "Jensen", "per.jensen@hotmail.org", "pasta");
        User user2 = new User("Jenny", "Jensen", "jenny.jensen@hotmail.org", "pasta");
        user1.setId(3L);
        user2.setId(3L);
        // Only the ID decides if two user objects are the same user
        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}
