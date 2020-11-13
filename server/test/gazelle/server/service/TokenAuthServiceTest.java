package gazelle.server.service;

import gazelle.server.TestHelper;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
import gazelle.server.error.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TokenAuthServiceTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private TokenAuthService tokenAuthService;

    @Test
    public void createTokenForUser() {
        assertThrows(UserNotFoundException.class, () -> {
            tokenAuthService.createTokenForUser(5000L);
        });

        Long userId = testHelper.createTestUser();
        String token = TokenAuthService.addBearer(
                tokenAuthService.createTokenForUser(userId));

        assertEquals(userId, tokenAuthService.getUserIdFromToken(token));

        // Create a new token, which should invalidate the first one
        String token2 = TokenAuthService.addBearer(
                tokenAuthService.createTokenForUser(userId));

        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserObjectFromToken(token);
        });

        assertEquals(userId, tokenAuthService.getUserIdFromToken(token2));

        testHelper.deleteTestUser(userId);
    }

    @Test
    public void getUserFromToken() {
        Long userId = testHelper.createTestUser();
        String token = testHelper.logInUser(userId);

        assertThrows(MissingAuthorizationException.class, () -> {
            tokenAuthService.getUserIdFromToken(null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserObjectFromToken("Bearer: dummy");
        });
        assertEquals(userId, tokenAuthService.getUserIdFromToken(token));
        assertEquals(userId, tokenAuthService.getUserObjectFromToken(token).getId());

        testHelper.deleteTestUser(userId);
    }

    @Test
    public void assertTokenForUser() {
        final Long userId = testHelper.createTestUser();
        final Long user2Id = testHelper.createTestUser();
        final String token = testHelper.logInUser(userId);

        tokenAuthService.assertTokenForUser(userId, token);

        assertThrows(MissingAuthorizationException.class, () -> {
            tokenAuthService.assertTokenForUser(userId, null);
        });
        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.assertTokenForUser(userId, "wrong-token");
        });
        assertThrows(AuthorizationException.class, () -> {
            tokenAuthService.assertTokenForUser(user2Id, token);
        });

        testHelper.deleteTestUser(userId);
        testHelper.deleteTestUser(user2Id);
    }

    @Test
    public void removeToken() {
        final Long userId = testHelper.createTestUser();
        final String token = testHelper.logInUser(userId);

        assertEquals(userId, tokenAuthService.getUserIdFromToken(token));

        tokenAuthService.removeToken(token);

        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.getUserIdFromToken(token);
        });

        assertThrows(InvalidTokenException.class, () -> {
            tokenAuthService.removeToken(token);
        });
        assertThrows(MissingAuthorizationException.class, () -> {
            tokenAuthService.removeToken(null);
        });

        //Test that creating a new token works
        final String token2 = TokenAuthService.addBearer(
                tokenAuthService.createTokenForUser(userId));

        assertEquals(userId, tokenAuthService.getUserIdFromToken(token2));

        testHelper.deleteTestUser(userId);
    }
}
