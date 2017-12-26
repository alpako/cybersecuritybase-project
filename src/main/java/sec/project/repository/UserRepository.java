package sec.project.repository;

import org.springframework.data.repository.CrudRepository;
import sec.project.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User getUserByUsername(String username);
}
