package gazelle.server.repository;

import gazelle.model.Chore;
import gazelle.model.User;
import gazelle.model.UserChoreProgress;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserChoreProgressRepository extends CrudRepository<UserChoreProgress, Long> {
    Optional<UserChoreProgress> findUserChoreProgressByUserAndChore(User user, Chore chore);
}
