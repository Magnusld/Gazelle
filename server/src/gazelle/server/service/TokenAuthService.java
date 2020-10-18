package gazelle.server.service;

import gazelle.model.TokenLogIn;
import gazelle.model.User;
import gazelle.server.error.InvalidTokenException;
import gazelle.server.repository.TokenLogInRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
     * @return the token created for the user
     */
    public String createTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        TokenLogIn tokenLogIn = new TokenLogIn(user, token);
        tokenRepository.save(tokenLogIn);
        return token;
    }

    /**
     * Gets the User object associated with an authorization token
     *
     * @param token the token
     * @return the user for which the token belongs
     * @throws InvalidTokenException if the token doesn't belong to a user
     */
    public User getUserForToken(String token) {
        Optional<User> user = tokenRepository.findUserByToken(token);
        return user.orElseThrow(InvalidTokenException::new);
    }
}
