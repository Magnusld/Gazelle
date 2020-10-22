package gazelle.server;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.endpoint.LoginEndpoint;
import gazelle.server.endpoint.UserController;
import gazelle.server.error.GazelleException;
import gazelle.server.error.LoginFailedException;
import gazelle.server.error.UserNotFoundException;
import gazelle.server.repository.UserRepository;
import gazelle.server.service.TokenAuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserController userController;

    @Test
    public void userRepositoryTest() {
        User user = userRepository
                .save(new User("niss1", "niss1"));
        Optional<User> foundUser = userRepository.findByUsername("niss1");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertTrue(userRepository.findById(user.getId()).isPresent());
        userRepository.delete(user);
        assertFalse(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void userControllerTest() {
        User user = userRepository.save(new User("niss4", "niss4"));
        assertEquals(userController.findAll().iterator().next().getUsername(),
                userRepository.findAll().iterator().next().getUsername());
        assertEquals(userController.findAll().iterator().next().getId(),
                userRepository.findAll().iterator().next().getId());
        assertEquals(user.getUsername(), userController.findByUsername("niss4").getUsername());

        User user1 = userRepository.save(new User("niss5", "niss5"));
        assertEquals(user1.getPassword(), userController.findByUsername("niss5").getPassword());
        assertEquals(user, userController.findByUsername("niss4"));
        assertThrows(UserNotFoundException.class,
                () -> userController.findByUsername("Jalla"));
        assertEquals(user.getUsername(), userController.findOne(user.getId()).getUsername());
        assertEquals(user.getPassword(), userController.findOne(user.getId()).getPassword());

        userRepository.delete(user);
        userRepository.delete(user1);
    }
}


