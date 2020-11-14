package gazelle.server.endpoint;

import gazelle.api.UserResponse;
import gazelle.model.User;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Makes a serializable object with user data.
     * Does not give out e-mail-addresses or passwords.
     *
     * @param user the user object
     * @return UserResponse for the user object
     */
    @Transactional(propagation = Propagation.MANDATORY)
    public UserResponse makeUserResponse(User user) {
        UserResponse.Builder builder = new UserResponse.Builder();
        builder.id(user.getId())
                .firstName(user.getFirstname())
                .lastName(user.getLastname());
        return builder.build();
    }

    @GetMapping
    @Transactional
    public List<UserResponse> findAll() {
        List<UserResponse> result = new ArrayList<>();
        userRepository.findAll().forEach(u -> result.add(makeUserResponse(u)));
        return result;
    }

    @GetMapping("/{id}")
    @Transactional
    public UserResponse findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(this::makeUserResponse)
                .orElseThrow(UserNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id,
                           @RequestHeader("Authorization") @Nullable String auth) {
        tokenAuthService.assertTokenForUser(id, auth);
        userRepository.deleteById(id);
    }
}

