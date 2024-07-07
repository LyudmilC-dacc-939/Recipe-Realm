package Project.Recipe_Realm.converter;

import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class RecipeConverter {

    private UserRepository userRepository;
    private RecipeRepository recipeRepository;

    public Recipe toRecipe(RecipeRequest recipeRequest){
        Recipe recipe = new Recipe();
        recipe.setCategory(recipeRequest.getCategory());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setCreatedAt(Instant.now());
        recipe.setIngredients(recipeRequest.getIngredients());
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setUser(userRepository.findById(recipeRequest.getUserId()).get());
        return recipe;
    }
}
