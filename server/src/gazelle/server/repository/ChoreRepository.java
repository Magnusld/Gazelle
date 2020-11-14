package gazelle.server.repository;

import gazelle.model.Chore;
import org.springframework.data.repository.CrudRepository;

public interface ChoreRepository extends CrudRepository<Chore, Long> {

}
