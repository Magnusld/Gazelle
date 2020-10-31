package gazelle.server.repository;

import gazelle.model.TokenLogIn;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenLogInRepository extends CrudRepository<TokenLogIn, Long> {
    Optional<TokenLogIn> findByToken(String token);
}
