package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.LoginRequest;
import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.service.UserService;
import Project.Recipe_Realm.service.impl.CurrentUserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@EnableMethodSecurity
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    private final CurrentUserServiceImpl currentUserService;

    @Autowired
    public UserController(UserService userService,
                          CurrentUserServiceImpl currentUserService) {
        this.userService = userService;
        this.currentUserService = currentUserService;
    }

    @Operation(summary = "Registers new user", description = "Returns registered user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Format is not valid",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Access denied due to Security Configuration",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))})
    @PostMapping(path = "/register")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userRequest));
    }

    @Operation(summary = "Gets a user by ID", description = "Returns user information based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class)))
    })
    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id,
                                                @AuthenticationPrincipal User currentUser) {
        User extractCurrentUser = currentUserService.extractCurrentUser();
        if (!extractCurrentUser.getId().equals(currentUser.getId()) || !currentUserService.isCurrentUserARole("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Gets all recipes for a user", description = "Returns all recipes created by the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipes found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "User or recipes not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class)))
    })
    @GetMapping(path = "/get-user-recipes/{id}")
    public ResponseEntity<Set<Recipe>> getAllRecipesForUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getAllRecipesForUser(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Gets favorite recipes for a user", description = "Returns all favorite recipes of the user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Favorite recipes found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class))),
            @ApiResponse(responseCode = "404", description = "User or favorite recipes not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recipe.class)))
    })
    @GetMapping(path = "/get-favorite-recipes/{id}")
    public ResponseEntity<Set<Recipe>> getAllFavoriteRecipesForUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getAllFavoriteRecipesForUser(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Updates a user by ID", description = "Updates user information based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserRequest.class)))
    })
    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserRequest userRequest,
                                           @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updateUser(userRequest, id), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Adds a recipe to user's favorites", description = "Adds a recipe to the user's favorite list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe added to favorites",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or recipe not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PatchMapping(path = "/add-to-favorites")
    public ResponseEntity<UserResponse> addToFavorites(@RequestParam("userId") Long userId,
                                                       @RequestParam("recipeId") Long recipeId) {
        if(currentUserService.isCurrentUserARole("ROLE_ADMIN")){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.addToFavorites(userId, recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Removes a recipe from user's favorites", description = "Removes a recipe from the user's favorite list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recipe removed from favorites",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or recipe not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class)))
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PatchMapping(path = "/remove-from-favorites")
    public ResponseEntity<UserResponse> removeFromFavorites(@RequestParam("userId") Long userId,
                                                            @RequestParam("recipeId") Long recipeId) {
        if(currentUserService.isCurrentUserARole("ROLE_ADMIN")){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.removeFromFavorites(userId, recipeId), HttpStatus.OK);
    }

    @Operation(summary = "Deletes a user by ID", description = "Deletes a user from the system based on ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Logs in a user", description = "Authenticates user credentials and returns a token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }
}
