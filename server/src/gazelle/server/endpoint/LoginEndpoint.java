package gazelle.server.endpoint;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.error.GazelleException;
import gazelle.server.error.LoginFailedException;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginEndpoint {

    private final UserRepository userRepository;

    @Autowired
    public LoginEndpoint(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public LogInResponse login(@RequestBody LogInRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        User user = userRepository.findByUsername(username)
                .orElseThrow(LoginFailedException::new);

        if (!user.getPassword().equals(password))
            throw new LoginFailedException();

        return new LogInResponse("dummy-token", user);
    }

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

        return new LogInResponse("summy-doken", newUser);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        //TODO
    }
}
