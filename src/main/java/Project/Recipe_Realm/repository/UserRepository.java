package Project.Recipe_Realm.repository;

import Project.Recipe_Realm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
