package Project.Recipe_Realm.service;

import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.dto.RecipeResponse;
import Project.Recipe_Realm.model.Recipe;

public interface RecipeService {

    RecipeResponse createRecipe (RecipeRequest recipeRequest);

    RecipeResponse getRecipe(Long id);

    Recipe updateRecipe(RecipeRequest recipeRequest, Long id);

    void deleteRecipe(Long id);

    RecipeResponse likeRecipe(Long id);

    RecipeResponse dislikeRecipe(Long id);

    RecipeResponse getAllCommentsFromRecipe(Long id);

}
