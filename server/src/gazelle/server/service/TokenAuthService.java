package gazelle.server.service;

import gazelle.model.TokenLogIn;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.repository.TokenLogInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * When a user is logged in, there is a token associated with the user.
 * When there is a token associated with a user, the user is logged in.
 *
 * <p>See: https://swagger.io/docs/specification/authentication/bearer-authentication/
 */
@Service
public class TokenAuthService {

    private final TokenLogInRepository tokenRepository;

    @Autowired
    public TokenAuthService(TokenLogInRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Creates an authorization token for a user.
     * Removes any previous token for that user.
     *
     * @param user the user to create a token for
     * @return the token created for the user (excluding Bearer-prefix)
     */
    public String createTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        TokenLogIn tokenLogIn = new TokenLogIn(user, token);
        tokenRepository.save(tokenLogIn);
        return token;
    }

    /**
     * Gets the User object associated with an authorization token.
     * Note that a previously valid token can become invalid at any time.
     *
     * @param token the token (including Bearer-prefix)
     * @return the user for which the token belongs
     * @throws InvalidTokenException if the token is malformed or doesn't belong to a user
     */
    public User getUserForToken(String token) {
        token = stripBearer(token);
        Optional<User> user = tokenRepository.findUserByToken(token);
        return user.orElseThrow(InvalidTokenException::new);
    }

    /**
     * Checks that the user object is the user we are logged in as
     *
     * @param user the user object we want to be logged in as
     * @param token the token we are authorized as (including Bearer-prefix)
     * @throws InvalidTokenException if the token is invalid
     * @throws AuthorizationException if the user is not the one we are logged in as
     */
    public void assertUserLoggedIn(User user, String token) {
        User authed = getUserForToken(token);
        if (!authed.equals(user))
            throw new AuthorizationException();
    }

    /**
     * Checks that the userId is the user we are logged in as
     *
     * @param userId the id of the user we want to be logged in as
     * @param token the token we are authorized as (including Bearer-prefix)
     * @return User the user object of the authenticated in user
     * @throws InvalidTokenException if the token is invalid
     * @throws AuthorizationException if the user is not the one we are logged in as
     */
    public User assertUserLoggedIn(Long userId, String token) {
        User authed = getUserForToken(token);
        if (!authed.getId().equals(userId))
            throw new AuthorizationException();
        return authed;
    }

    /**
     * Removes a token from the database, effectively logging the user out.
     *
     * @param token the token (including Bearer-prefix)
     * @throws InvalidTokenException if the token is malformed or doesn't belong to a user
     */
    public void removeToken(String token) {
        token = stripBearer(token);
        if (!tokenRepository.existsByToken(token))
            throw new InvalidTokenException();
        tokenRepository.deleteByToken(token);
    }

    /**
     * When tokens are sent in the Authorization header,
     * they are on the format: "Bearer &lt;token&gt;"
     *
     * <p>See: https://swagger.io/docs/specification/authentication/bearer-authentication/
     */
    public static final String BEARER_PREFIX = "Bearer ";

    public static String stripBearer(String token) {
        if (!token.startsWith(BEARER_PREFIX))
            throw new InvalidTokenException(
                    String.format("Not a Bearer token (missing prefix: '%s')", BEARER_PREFIX));
        return token.substring(BEARER_PREFIX.length());
    }

    public static String addBearer(String token) {
        return String.format("%s%s", BEARER_PREFIX, token);
    }
}
