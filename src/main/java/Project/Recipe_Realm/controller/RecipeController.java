package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.RecipeCommentsResponse;
import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.dto.RecipeResponse;
import Project.Recipe_Realm.dto.RecipeUpdateRequest;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.service.RecipeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/recipe")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        return new ResponseEntity<>(recipeService.createRecipe(recipeRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.FOUND);
    }

    @GetMapping(path = "/get-comments/{id}")
    public ResponseEntity<RecipeCommentsResponse> getAllCommentsFromRecipe(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.getAllCommentsFromRecipe(id), HttpStatus.FOUND);
    }

    @GetMapping(path = "/get-users-favorited/{id}")
    public ResponseEntity<Set<User>> getUsers(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.getUsers(id), HttpStatus.FOUND);
    }

    @GetMapping(path = "/find-by")
    public ResponseEntity<List<Recipe>> findRecipeBy(@RequestParam("title") String title,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("category") String category) {
        return new ResponseEntity<>(recipeService.findRecipeBy(title, description, category), HttpStatus.FOUND);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest,
                                                       @PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.updateRecipe(recipeUpdateRequest, id), HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/like")
    public ResponseEntity<RecipeCommentsResponse> likeRecipe(@RequestParam("recipeId") Long recipeId,
                                                             @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(recipeService.likeRecipe(recipeId, userId), HttpStatus.OK);
    }

    @PutMapping(path = "/dislike")
    public ResponseEntity<RecipeCommentsResponse> dislikeRecipe(@RequestParam("recipeId") Long recipeId,
                                                                @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(recipeService.dislikeRecipe(recipeId, userId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") Long id){
        recipeService.deleteRecipe(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
