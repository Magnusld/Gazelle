package gazelle.server;

import gazelle.auth.LogInRequest;
import gazelle.auth.LogInResponse;
import gazelle.auth.SignUpRequest;
import gazelle.model.User;
import gazelle.server.endpoint.LoginEndpoint;
import gazelle.server.error.GazelleException;
import gazelle.server.error.LoginFailedException;
import gazelle.server.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginEndpoint loginEndpoint;

    @Test
    public void signUpNewUserTest() {
        SignUpRequest signUpRequest = new SignUpRequest("niss2", "niss2");
        loginEndpoint.signup(signUpRequest);
        assertTrue(userRepository.findByUsername("niss2").isPresent());
        GazelleException exception1 = assertThrows(GazelleException.class,
                () -> loginEndpoint.signup(new SignUpRequest("niss2", "niss2")));
        GazelleException exception2 = assertThrows(GazelleException.class,
                () -> loginEndpoint.signup(new SignUpRequest("niss4","nis")));
        assertEquals(exception1.getReason(),"Username taken");
        assertEquals(exception2.getReason(),"Password too short");
    }

    @Test
    public void logInUserTest() {
        User user = userRepository.save(new User("niss3","niss3"));
        LogInRequest logInRequest = new LogInRequest("niss3","niss3");
        LogInResponse logInResponse = loginEndpoint.login(logInRequest);
        assertEquals(logInResponse.getToken(),"dummy-token");
        Throwable exception = assertThrows(LoginFailedException.class,
                () -> loginEndpoint.login(new LogInRequest("nise", "nise")));
    }


    @Test
    public void userRepositoryTest() {
        User user = userRepository
                .save(new User("niss1","niss1"));
        Optional<User> foundUser = userRepository.findByUsername("niss1");
        assertTrue(foundUser.isPresent());
        assertEquals(user.getId(), foundUser.get().getId());
        assertTrue(userRepository.findById(user.getId()).isPresent());
    }

    @Test
    public void testDatabaseTest() {
        assertFalse(userRepository.findByUsername("nissen").isPresent());
    }

}
