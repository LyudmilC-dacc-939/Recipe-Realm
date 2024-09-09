package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.RecipeCommentsResponse;
import Project.Recipe_Realm.dto.RecipeRequest;
import Project.Recipe_Realm.dto.RecipeResponse;
import Project.Recipe_Realm.dto.RecipeUpdateRequest;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/recipes")
public class RecipeController {

    private RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Creates a new recipe", description = "Returns the created recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recipe successfully created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Resource not found")
    })
    @PostMapping
    public ResponseEntity<RecipeResponse> createRecipe(@Valid @RequestBody RecipeRequest recipeRequest) {
        return new ResponseEntity<>(recipeService.createRecipe(recipeRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Gets a recipe by ID", description = "Returns the recipe for the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<RecipeResponse> getRecipe(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.getRecipe(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Gets all comments for a recipe", description = "Returns all comments for the given recipe ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeCommentsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping(path = "/get-comments/{id}")
    public ResponseEntity<RecipeCommentsResponse> getAllCommentsFromRecipe(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.getAllCommentsFromRecipe(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Gets users who favorited the recipe", description = "Returns a list of users who have favorited the given recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping(path = "/get-users-favorite/{id}")
    public ResponseEntity<Set<User>> getUsers(@PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.getUsers(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Finds recipes by title, description, or category", description = "Search for recipes using title, description, or category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipes not found")
    })
    @GetMapping(path = "/find-by/")
    public ResponseEntity<List<Recipe>> findRecipeBy(@RequestParam(value = "title", required = false) String title,
                                                     @RequestParam(value = "description", required = false) String description,
                                                     @RequestParam(value = "category", required = false) String category) {
        return new ResponseEntity<>(recipeService.findRecipeBy(title, description, category), HttpStatus.FOUND);
    }

    @Operation(summary = "Updates a recipe", description = "Updates the details of a recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Recipe successfully updated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<RecipeResponse> updateRecipe(@Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest,
                                                       @PathVariable("id") Long id) {
        return new ResponseEntity<>(recipeService.updateRecipe(recipeUpdateRequest, id), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Likes a recipe", description = "Adds a like to the recipe by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe liked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeCommentsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe or user not found")
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PutMapping(path = "/like")
    public ResponseEntity<RecipeCommentsResponse> likeRecipe(@RequestParam("recipeId") Long recipeId,
                                                             @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(recipeService.likeRecipe(recipeId, userId), HttpStatus.OK);
    }

    @Operation(summary = "Dislikes a recipe", description = "Adds a dislike to the recipe by the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe disliked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeCommentsResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe or user not found")
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PutMapping(path = "/dislike")
    public ResponseEntity<RecipeCommentsResponse> dislikeRecipe(@RequestParam("recipeId") Long recipeId,
                                                                @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(recipeService.dislikeRecipe(recipeId, userId), HttpStatus.OK);
    }

    @Operation(summary = "Deletes a recipe", description = "Deletes a recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Recipe deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("id") Long id){
        recipeService.deleteRecipe(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
