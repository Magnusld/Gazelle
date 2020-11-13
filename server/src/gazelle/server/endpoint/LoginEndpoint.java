package gazelle.server.endpoint;

import gazelle.api.auth.LogInRequest;
import gazelle.api.auth.LogInResponse;
import gazelle.api.auth.SignUpRequest;
import gazelle.model.ModelException;
import gazelle.model.User;
import gazelle.server.error.GazelleException;
import gazelle.server.error.LoginFailedException;
import gazelle.server.error.UnprocessableEntityException;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginEndpoint {

    private final UserRepository userRepository;

    private final TokenAuthService tokenAuthService;

    @Autowired
    public LoginEndpoint(UserRepository userRepository, TokenAuthService tokenAuthService) {
        this.userRepository = userRepository;
        this.tokenAuthService = tokenAuthService;
    }

    /**
     * Authenticates as a user with username and password,
     * creates a token for the session, and returns it to the user.
     * Also includes the user object, which contains basic user info.
     *
     * <p>As of now, only one token can exists per user,
     * so any other token is invalidated (a token is never returned twice).
     *
     * @param request the credentials used to log in
     * @return the user object and token for the logged in user
     * @throws LoginFailedException if the username or password is wrong
     */
    @PostMapping("/login")
    @Transactional
    public LogInResponse login(@RequestBody LogInRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        User user = userRepository.findByEmail(email)
                .orElseThrow(LoginFailedException::new);

        if (!user.getPassword().equals(password))
            throw new LoginFailedException();

        String token = tokenAuthService.createTokenForUser(user);
        return new LogInResponse(token, user);
    }

    /**
     * Authenticates a user with an existing token.
     * If the token is still valid, returns the User object the token belongs to.
     * If the token is invalid, returns 401 (Unauthorized)
     *
     * @param auth the authentication token
     * @return the user owning the token
     * @throws gazelle.server.error.InvalidTokenException if the token is not valid
     */
    @GetMapping("/login")
    @Transactional
    public User loginWithToken(@RequestHeader("Authorization") @Nullable String auth) {
        return tokenAuthService.getUserObjectFromToken(auth);
    }

    /**
     * Creates a new user with the given password,
     * then creates a token for that user.
     * @param request the information about the new user
     * @return the new user and its token
     * @throws GazelleException if the name or password is denied (too short, taken etc.)
     */
    @PostMapping("/signup")
    @Transactional
    public LogInResponse signup(@RequestBody SignUpRequest request) {
        User user = request.buildUser();
        try {
            user.validate();
        } catch (ModelException e) {
            throw new UnprocessableEntityException(e.getMessage());
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new GazelleException("Email taken", HttpStatus.CONFLICT);

        userRepository.save(user);
        String token = tokenAuthService.createTokenForUser(user);
        return new LogInResponse(token, user);
    }

    /**
     * Logs a user out by invalidating their token
     *
     * @param auth the Authorization header containing the Bearer token
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") @Nullable String auth) {
        tokenAuthService.removeToken(auth);
    }
}
