package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.LoginRequest;
import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@EnableMethodSecurity
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @GetMapping(path = "/get-user-recipes/{id}")
    public ResponseEntity<Set<Recipe>> getAllRecipesForUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getAllRecipesForUser(id), HttpStatus.FOUND);
    }

    @GetMapping(path = "/get-favorite-recipes/{id}")
    public ResponseEntity<Set<Recipe>> getAllFavoriteRecipesForUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getAllFavoriteRecipesForUser(id), HttpStatus.FOUND);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserRequest userRequest,
                                           @PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.updateUser(userRequest, id), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MODERATOR')")
    @PatchMapping(path = "/add-to-favorites")
    public ResponseEntity<UserResponse> addToFavorites(@RequestParam("userId") Long userId,
                                                       @RequestParam("recipeId") Long recipeId) {
        return new ResponseEntity<>(userService.addToFavorites(userId, recipeId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MODERATOR')")
    @PatchMapping(path = "/remove-from-favorites")
    public ResponseEntity<UserResponse> removeFromFavorites(@RequestParam("userId") Long userId,
                                                            @RequestParam("recipeId") Long recipeId) {
        return new ResponseEntity<>(userService.removeFromFavorites(userId, recipeId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(userService.login(loginRequest), HttpStatus.OK);
    }
}
