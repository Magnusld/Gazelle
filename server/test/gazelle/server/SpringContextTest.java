package gazelle.server;

import gazelle.server.endpoint.LoginEndpoint;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Tests that loading the application context and running the app works
 * https://www.baeldung.com/spring-boot-start
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringContextTest {
    @Test
    public void contextLoads() {
    }
}