package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.advice.exception.RecordNotFoundException;
import Project.Recipe_Realm.converter.RecipeConverter;
import Project.Recipe_Realm.dto.RecipeCommentsResponse;
import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.dto.RecipeResponse;
import Project.Recipe_Realm.dto.RecipeUpdateRequest;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.repository.UserRepository;
import Project.Recipe_Realm.service.RecipeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class RecipeServiceImpl implements RecipeService {

    private RecipeRepository recipeRepository;
    private RecipeConverter recipeConverter;
    private UserRepository userRepository;

    @Autowired
    public RecipeServiceImpl(RecipeRepository recipeRepository,
                             RecipeConverter recipeConverter,
                             UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeConverter = recipeConverter;
        this.userRepository = userRepository;
    }

    @Override
    public RecipeResponse createRecipe(RecipeRequest recipeRequest) {
        User existingUser = userRepository.getReferenceById(recipeRequest.getUserId());
        if (existingUser == null) {
            throw new RecordNotFoundException(String.format("User with id %s not exist", recipeRequest.getUserId()));
        }
        Recipe recipe = recipeConverter.toRecipe(recipeRequest);
        recipe.setUser(existingUser);
        recipeRepository.save(recipe);

        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(recipe, recipeResponse);
        return recipeResponse;
    }

    @Override
    public RecipeResponse getRecipe(Long id) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with id %s not exist", id)));
        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(recipe, recipeResponse);
        System.out.println(HttpStatus.FOUND);
        return recipeResponse;
    }

    @Override
    public Recipe updateRecipe(RecipeUpdateRequest updateRequest, Long id) {
        Recipe existingRecipe = recipeRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with id %s not exist", id)));
        existingRecipe.setTitle(updateRequest.getTitle());
        existingRecipe.setDescription(updateRequest.getDescription());
        existingRecipe.setIngredients(updateRequest.getIngredients());
        existingRecipe.setCategory(updateRequest.getCategory());
        recipeRepository.save(existingRecipe);
        System.out.println(HttpStatus.ACCEPTED);
        return existingRecipe;
    }

    @Override
    public void deleteRecipe(Long id) {
        recipeRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException("Invalid ID, Deletion aborted"));
        recipeRepository.deleteById(id);
    }

    @Override
    public RecipeResponse likeRecipe(Long recipeId, Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", userId)));
        Recipe existingRecipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with id %s not exist", recipeId)));
        if (existingRecipe.getUsersIdDisliked().contains(userId)) {
            existingRecipe.getUsersIdDisliked().remove(userId);
            existingRecipe.setDislikes(existingRecipe.getDislikes() - 1);
        }
        existingRecipe.getUsersIdLiked().add(userId);
        existingRecipe.setLikes(existingRecipe.getLikes() + 1);
        recipeRepository.save(existingRecipe);

        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(existingRecipe, recipeResponse);
        System.out.println(recipeResponse);
        return recipeResponse;
    }

    @Override
    public RecipeResponse dislikeRecipe(Long recipeId, Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", userId)));
        Recipe existingRecipe = recipeRepository.findById(recipeId).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with id %s not exist", recipeId)));
        if (existingRecipe.getUsersIdLiked().contains(userId)) {
            existingRecipe.getUsersIdLiked().remove(userId);
            existingRecipe.setLikes(existingRecipe.getLikes() - 1);
        }
        existingRecipe.getUsersIdDisliked().add(userId);
        existingRecipe.setDislikes(existingRecipe.getDislikes() + 1);
        recipeRepository.save(existingRecipe);

        RecipeResponse recipeResponse = new RecipeResponse();
        BeanUtils.copyProperties(existingRecipe, recipeResponse);
        System.out.println(recipeResponse);
        return recipeResponse;

    }

    @Override
    public RecipeCommentsResponse getAllCommentsFromRecipe(Long id) {
        Recipe currentRecipe = recipeRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with ID: %s not exist", id)));
        System.out.println(HttpStatus.OK);
        RecipeCommentsResponse commentsResponse = new RecipeCommentsResponse();
        BeanUtils.copyProperties(currentRecipe, commentsResponse);
        return commentsResponse;
    }

    @Override
    public Set<User> getUsers(Long id) {
        Recipe currentRecipe = recipeRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with ID: %s not exist", id)));
        System.out.println(HttpStatus.OK);
        return currentRecipe.getUsers();
    }

    @Override
    public List<Recipe> findRecipeBy(String title, String description, String category) {
        return recipeRepository.findByTitleOrDescriptionOrCategory(title, description, category);
    }
}
