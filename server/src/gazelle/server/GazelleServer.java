package gazelle.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("gazelle.server.repository")
@EntityScan("gazelle.model")
@SpringBootApplication
public class GazelleServer {
    public static void main(String[] args) {
        SpringApplication.run(GazelleServer.class, args);
    }
}
