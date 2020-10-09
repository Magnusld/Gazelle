package gazelle.server;

import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.endpoint.LoginEndpoint;
import gazelle.server.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    private LoginEndpoint loginEndpoint = new LoginEndpoint(userRepository);

    @Test
    public void signUpNewUserTest() {
        SignUpRequest signUpRequest =
                new SignUpRequest("niss2","niss2");
        assertEquals(loginEndpoint.signup(signUpRequest),
                new LogInResponse("summy doken",userRepository.findByUsername("niss2")
                        .stream().findAny().get()));
    }

    @Test
    public void userRepositoryTest() {
        User user = userRepository
                .save(new User("niss1","niss1"));
        Optional<User> foundUser = userRepository.findByUsername("niss1");
        assertTrue(foundUser.isPresent());
        assertTrue(user.getId().equals(userRepository.findByUsername("niss1")
                .stream().findAny().get().getId()));
        userRepository.delete(user);
    }

    @Test
    public void logInUserTest() {

    }

    @Test
    public void testDatabaseTest() {
        assertFalse(userRepository.findByUsername("nissen").isPresent());
    }

}
