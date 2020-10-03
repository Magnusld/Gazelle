package gazelle.server.endpoint;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.model.User;
import gazelle.server.error.LoginFailedException;
import gazelle.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginEndpoint {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public LogInResponse login(@RequestBody LogInRequest request) {
        String username = request.getUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(LoginFailedException::new);

        if(!user.getPassword().equals(request.getPassword()))
            throw new LoginFailedException();

        return new LogInResponse("dummy-token", user);
    }
}
