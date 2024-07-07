package Project.Recipe_Realm.repository;

import Project.Recipe_Realm.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Override
    Recipe getReferenceById(Long aLong);

//todo   getAllRecipes(with parameters and search)
//Set<Recipe> getRecipesBy();
}
