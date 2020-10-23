package gazelle.server.repository;

import gazelle.model.TokenLogIn;
import gazelle.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenLogInRepository extends CrudRepository<TokenLogIn, Long> {
    @Query("SELECT tli.user FROM TokenLogIn tli WHERE tli.token = :token")
    Optional<User> findUserByToken(@Param("token") String token);

    Optional<TokenLogIn> findTokenLogInByToken(String token);

    Optional<TokenLogIn> findTokenLogInByUser(User user);

    boolean existsByToken(String token);

    void deleteByToken(String token);
}
