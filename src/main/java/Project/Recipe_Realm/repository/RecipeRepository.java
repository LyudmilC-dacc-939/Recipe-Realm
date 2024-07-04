package Project.Recipe_Realm.repository;

import Project.Recipe_Realm.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
}
