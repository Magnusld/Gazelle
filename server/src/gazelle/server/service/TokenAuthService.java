package gazelle.server.service;

import gazelle.model.TokenLogIn;
import gazelle.model.User;
import gazelle.server.error.AuthorizationException;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.error.MissingAuthorizationException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.TokenLogInRepository;
import gazelle.server.repository.UserRepository;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserRepository userRepository;

    @Autowired
    public TokenAuthService(TokenLogInRepository tokenRepository, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates an authorization token for a user.
     * Removes any previous token for that user.
     *
     * @param user the user to create a token for
     * @return the token created for the user (excluding Bearer-prefix)
     */
    public String createTokenForUser(User user) {
        TokenLogIn previous = user.getToken();
        if (previous != null)
            tokenRepository.delete(previous);

        String token = UUID.randomUUID().toString();
        TokenLogIn tokenLogIn = new TokenLogIn(user, token);
        user.setToken(tokenLogIn);
        return token;
    }

    /**
     * Creates an authorization token for a user
     * @param userId the is of the user to create a token for
     * @return the created token (excluding Bearer-prefix)
     */
    @Transactional
    public String createTokenForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return createTokenForUser(user);
    }

    /**
     * Gets the User-object owning a token
     *
     * @param token the token (including "Bearer "-prefix)
     * @return the user object owning the token
     * @throws InvalidTokenException if the token doesn't belong to any user
     */
    public User getUserObjectFromToken(@Nullable String token) {
        assertTokenNonNull(token);
        token = stripBearer(token);
        return userRepository.findByToken_Token(token)
                .orElseThrow(InvalidTokenException::new);
    }

    /**
     * Gets the id of the user owning a token
     *
     * @param token the token (including "Bearer "-prefix)
     * @return the id of the user owning the token
     * @throws InvalidTokenException if the token doesn't belong to any user
     */
    @Transactional
    public Long getUserIdFromToken(@Nullable String token) {
        return getUserObjectFromToken(token).getId();
    }

    /**
     * Checks that the token is a valid credential for the userId
     *
     * @param userId the id of the user we want to be logged in as
     * @param token the token we are authorized as (including Bearer-prefix)
     * @throws InvalidTokenException if the token is invalid
     * @throws AuthorizationException if the user is not the one we are logged in as
     */
    public void assertTokenForUser(Long userId, @Nullable String token) {
        Long authedId = getUserIdFromToken(token);
        if (!authedId.equals(userId))
            throw new AuthorizationException();
    }

    /**
     * Removes a token from the database, effectively logging the user out.
     *
     * @param token the token (including Bearer-prefix)
     * @throws InvalidTokenException if the token is malformed or doesn't belong to a user
     */
    @Transactional
    public void removeToken(@Nullable String token) {
        TokenAuthService.assertTokenNonNull(token);
        token = stripBearer(token);
        TokenLogIn row = tokenRepository.findByToken(token)
                .orElseThrow(InvalidTokenException::new);
        if (row.getUser() != null)
            row.getUser().setToken(null);
        tokenRepository.delete(row);
    }

    /**
     * When tokens are sent in the Authorization header,
     * they are on the format: "Bearer &lt;token&gt;"
     *
     * <p>See: https://swagger.io/docs/specification/authentication/bearer-authentication/
     */
    public static final String BEARER_PREFIX = "Bearer ";

    /**
     * Removes the prefix "Bearer " from the string token and returns whats left.
     * Throws if the string doesn't have the prefix.
     *
     * @param token with "Bearer " prefix
     * @return token with prefix removed
     * @throws InvalidTokenException if token doesn't have Bearer prefix
     */
    public static String stripBearer(String token) {
        if (!token.startsWith(BEARER_PREFIX))
            throw new InvalidTokenException(
                    String.format("Not a Bearer token (missing prefix: '%s')", BEARER_PREFIX));
        return token.substring(BEARER_PREFIX.length());
    }

    /**
     * Adds the prefix "Bearer " to the supplied string.
     *
     * @param token the text to be prefixed
     * @return the token with a "Bearer " prefix
     */
    public static String addBearer(String token) {
        return String.format("%s%s", BEARER_PREFIX, token);
    }

    /**
     * Throws 401 (Unauthorized) if the token is null
     *
     * @param token to be checked
     * @throws MissingAuthorizationException if token is null
     */
    @Contract("null -> fail")
    public static void assertTokenNonNull(@Nullable String token) {
        if (token == null)
            throw new MissingAuthorizationException();
    }
}
