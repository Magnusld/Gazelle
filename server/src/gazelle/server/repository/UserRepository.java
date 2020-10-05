package gazelle.server.repository;

import gazelle.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(long id);

    Optional<User> findByUsername(String username);
}
