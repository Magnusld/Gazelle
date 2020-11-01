package gazelle.server.endpoint;

import gazelle.model.User;
import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.UserNotFoundException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private TestHelper testHelper;

    private User user1;
    private User user2;

    @BeforeAll
    public void setup() {
        user1 = testHelper.createTestUserObject();
        user2 = testHelper.createTestUserObject();
    }

    @Test
    public void findAll() {
        List<User> users = new ArrayList<>();
        userController.findAll().forEach(users::add);
        assertEquals(2, users.size());
        assertTrue(users.contains(user1));
        assertTrue(users.contains(user2));
    }

    @Test
    public void findById() {
        assertEquals(user1, userController.findOne(user1.getId()));
        assertThrows(UserNotFoundException.class, () -> {
            userController.findOne(5000L);
        });
    }

    @Test
    public void findByUsername() {
        assertEquals(user1, userController.findByUsername(user1.getUsername()));
        assertThrows(UserNotFoundException.class, () -> {
            userController.findByUsername("dummy name");
        });
    }

    @Test
    public void deleteUser() {
        String token = testHelper.logInUser(user1.getId());
        assertThrows(InvalidTokenException.class, () -> {
            userController.deleteUser(user1.getId(), "Bearer: dummy token");
        });
        assertThrows(AuthorizationException.class, () -> {
            userController.deleteUser(user2.getId(), token);
        });
        userController.deleteUser(user1.getId(), token);
        assertThrows(InvalidTokenException.class, () -> {
            userController.deleteUser(user1.getId(), token);
        });
        user1 = testHelper.createTestUserObject();
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestUser(user1.getId());
        testHelper.deleteTestUser(user2.getId());
    }
}
