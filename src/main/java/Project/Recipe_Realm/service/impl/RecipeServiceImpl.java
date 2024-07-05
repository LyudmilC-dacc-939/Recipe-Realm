package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.dto.RecipeResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.service.RecipeService;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {
    @Override
    public RecipeResponse createRecipe(RecipeRequest recipeRequest) {
        return null;
    }

    @Override
    public RecipeResponse getRecipe(Long id) {
        return null;
    }

    @Override
    public Recipe updateRecipe(RecipeRequest recipeRequest, Long id) {
        return null;
    }

    @Override
    public void deleteRecipe(Long id) {

    }

    @Override
    public RecipeResponse likeRecipe(Long id) {
        return null;
    }

    @Override
    public RecipeResponse dislikeRecipe(Long id) {
        return null;
    }

    @Override
    public RecipeResponse getAllCommentsFromRecipe(Long id) {
        return null;
    }
}
