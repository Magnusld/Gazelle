package gazelle.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Tests that loading the application context and running the app works
 *
 * <p>See: https://www.baeldung.com/spring-boot-start
 * <br> JUnit4 -> JUnit 5: https://www.baeldung.com/junit-5-runwith
 * <br> JUnit5-ified: https://dev.to/martinbelev/how-to-enable-junit-5-in-new-spring-boot-project-29a8
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SpringContextTest {
    @Test
    public void contextLoads() {
    }
}