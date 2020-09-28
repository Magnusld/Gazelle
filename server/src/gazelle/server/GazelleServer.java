package gazelle.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The entry point for the server, based on
 * https://www.baeldung.com/spring-boot-start
 *
 * The SpringBootApplication-annotation does some auto-configuration,
 * but only looks for components in this package and its subpackages.
 *
 * Therefore we must specify EntityScan manually.
 * See: https://springbootdev.com/2017/11/13/what-are-the-uses-of-entityscan-and-enablejparepositories-annotations/
 */
@EnableJpaRepositories("gazelle.server.repository")
@EntityScan("gazelle.model")
@SpringBootApplication
public class GazelleServer {
    public static void main(String[] args) {
        SpringApplication.run(GazelleServer.class, args);
    }
}
