package gazelle.server.endpoint;

import gazelle.model.User;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    private final TokenAuthService tokenAuthService;

    @Autowired
    public UserController(UserRepository userRepository, TokenAuthService tokenAuthService) {
        this.userRepository = userRepository;
        this.tokenAuthService = tokenAuthService;
    }

    @GetMapping
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/email/{email}")
    public User findByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id,
                           @RequestHeader("Authorization") @Nullable String auth) {
        tokenAuthService.assertTokenForUser(id, auth);
        userRepository.deleteById(id);
    }
}

