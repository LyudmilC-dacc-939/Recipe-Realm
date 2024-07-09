package Project.Recipe_Realm.service;

import Project.Recipe_Realm.dto.RecipeCommentsResponse;
import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.dto.RecipeResponse;
import Project.Recipe_Realm.dto.RecipeUpdateRequest;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;

import java.util.List;
import java.util.Set;

public interface RecipeService {

    RecipeResponse createRecipe (RecipeRequest recipeRequest);

    RecipeResponse getRecipe(Long id);

    Recipe updateRecipe(RecipeUpdateRequest recipeUpdateRequest, Long id);

    void deleteRecipe(Long id);

    RecipeResponse likeRecipe(Long recipeId, Long userId);

    RecipeResponse dislikeRecipe(Long recipeId, Long userId);

    RecipeCommentsResponse getAllCommentsFromRecipe(Long id);

    Set<User> getUsers(Long id);

    List<Recipe> findRecipeBy(String title, String description, String category);
}
