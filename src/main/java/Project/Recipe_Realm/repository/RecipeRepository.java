package Project.Recipe_Realm.repository;

import Project.Recipe_Realm.model.Recipe;
import jdk.jfr.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Override
    Recipe getReferenceById(Long aLong);

    Recipe getReferenceByCategoryOrIngredientsOrCreatedAtOrTitle(String category, String Ingredients, Instant time, String title);
}
