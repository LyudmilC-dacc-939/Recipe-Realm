package Project.Recipe_Realm.repository;

import Project.Recipe_Realm.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
