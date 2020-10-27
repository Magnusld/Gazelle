package gazelle.server.endpoint;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.error.GazelleException;
import gazelle.server.error.LoginFailedException;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public LogInResponse login(@RequestBody LogInRequest request) {
        Objects.requireNonNull(request);
        String username = request.getUsername();
        String password = request.getPassword();

        User user = userRepository.findByUsername(username)
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
    public User loginWithToken(@RequestHeader("Authorization") String auth) {
        return tokenAuthService.getUserForToken(auth);
    }

    /**
     * Creates a new user with the given password,
     * then creates a token for that user.
     * @param request the information about the new user
     * @return the new user and its token
     * @throws GazelleException if the name or password is denied (too short, taken etc.)
     */
    @PostMapping("/signup")
    public LogInResponse signup(@RequestBody SignUpRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        if (userRepository.findByUsername(username).isPresent())
            throw new GazelleException("Username taken", username, HttpStatus.CONFLICT);

        if (password.length() < 4)
            throw new GazelleException("Password too short", null, HttpStatus.UNPROCESSABLE_ENTITY);

        if (username.length() < 4)
            throw new GazelleException("Username too short", null, HttpStatus.UNPROCESSABLE_ENTITY);

        User newUser = new User(username, password);
        userRepository.save(newUser);

        String token = tokenAuthService.createTokenForUser(newUser);

        return new LogInResponse(token, newUser);
    }

    /**
     * Logs a user out by invalidating their token
     *
     * @param auth the Authorization header containing the Bearer token
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String auth) {
        tokenAuthService.removeToken(auth);
    }
}
