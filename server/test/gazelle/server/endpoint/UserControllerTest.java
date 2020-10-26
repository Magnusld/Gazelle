package gazelle.server.endpoint;

import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.service.TokenAuthService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @Autowired
    private UserController userController;

    @Autowired
    private LoginEndpoint loginEndpoint;

    private static final String NAME1 = "username1";
    private static final String NAME2 = "username2";

    private User user1;
    private String user1Token;
    private User user2;
    private String user2Token;

    @BeforeAll
    public void setup() {
        LogInResponse response = loginEndpoint.signup(new SignUpRequest(NAME1, "fkdlgjrg"));
        user1 = response.getUser();
        user1Token = TokenAuthService.addBearer(response.getToken());
        response = loginEndpoint.signup(new SignUpRequest(NAME2, "fkdlgjrg"));
        user2 = response.getUser();
        user2Token = TokenAuthService.addBearer(response.getToken());
    }

    @Test
    public void testFindAll() {
        int user1Seen = 0;
        int user2Seen = 0;
        for (User user : userController.findAll()) {
            user1Seen += user.equals(user1) ? 0 : 1;
            user2Seen += user.equals(user2) ? 0 : 1;
        }
        assertEquals(1, user1Seen);
        assertEquals(1, user2Seen);
    }

    @Test
    public void testFindByUsername() {
        assertEquals(user1, userController.findByUsername(user1.getUsername()));
        assertThrows(UserNotFoundException.class, () -> {
            userController.findByUsername("dummy username");
        });
    }

    @Test
    public void testFindOne() {
        assertEquals(user1, userController.findOne(user1.getId()));
        assertEquals(user2, userController.findOne(user2.getId()));
        assertThrows(UserNotFoundException.class, () -> {
            userController.findOne(5001L);
        });
    }

    @Test
    public void testDeleteUser() {
        assertThrows(MissingAuthorizationException.class, () -> {
            userController.deleteUser(user1.getId(), null);
        });
        assertThrows(AuthorizationException.class, () -> {
            userController.deleteUser(user1.getId(), user2Token);
        });
        assertThrows(InvalidTokenException.class, () -> {
            userController.deleteUser(user1.getId(), "dummy-token");
        });
        assertEquals(user1, userController.findOne(user1.getId()));

        userController.deleteUser(user1.getId(), user1Token);
        assertThrows(UserNotFoundException.class, () -> {
            userController.findByUsername(NAME1);
        });
        assertThrows(UserNotFoundException.class, () -> {
            userController.findOne(user1.getId());
        });

        // Make a new user to uphold test invariant
        LogInResponse response = loginEndpoint.signup(new SignUpRequest(NAME1, "gjsekf"));
        user1 = response.getUser();
        user1Token = TokenAuthService.addBearer(response.getToken());
    }

    @AfterAll
    public void cleanup() {
        userController.deleteUser(user1.getId(), user1Token);
        userController.deleteUser(user2.getId(), user2Token);
    }
}
