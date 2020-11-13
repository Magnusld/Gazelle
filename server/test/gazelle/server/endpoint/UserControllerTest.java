package gazelle.server.endpoint;

import gazelle.api.UserResponse;
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

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private UserController userController;

    @Autowired
    private TestHelper testHelper;

    private Long user1;
    private Long user2;

    @BeforeAll
    public void setup() {
        user1 = testHelper.createTestUser();
        user2 = testHelper.createTestUser();
    }

    @Test
    public void findAll() {
        List<UserResponse> users = userController.findAll();
        assertTrue(users.stream().anyMatch(it -> Objects.equals(it.getId(), user1)));
        assertTrue(users.stream().anyMatch(it -> Objects.equals(it.getId(), user2)));
    }

    @Test
    public void findById() {
        assertThrows(UserNotFoundException.class, () -> {
            userController.findById(5000L);
        });
        User user = testHelper.createTestUserObject();
        UserResponse response = userController.findById(user.getId());
        assertEquals(user.getId(), response.getId());
        assertEquals(user.getFirstname(), response.getFirstName());
        assertEquals(user.getLastname(), response.getLastName());
        testHelper.deleteTestUser(user);
    }

    @Test
    public void deleteUser() {
        String token = testHelper.logInUser(user1);
        assertThrows(InvalidTokenException.class, () -> {
            userController.deleteUser(user1, "Bearer: dummy token");
        });
        assertThrows(AuthorizationException.class, () -> {
            userController.deleteUser(user2, token);
        });
        userController.deleteUser(user1, token);
        assertThrows(InvalidTokenException.class, () -> {
            userController.deleteUser(user1, token);
        });
        user1 = testHelper.createTestUser();
    }

    @AfterAll
    public void cleanup() {
        testHelper.deleteTestUser(user1);
        testHelper.deleteTestUser(user2);
    }
}
