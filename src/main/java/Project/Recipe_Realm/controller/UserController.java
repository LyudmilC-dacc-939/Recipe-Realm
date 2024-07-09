package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    //todo: login mapping

    @GetMapping(path = "/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.FOUND);
    }

    @GetMapping(path = "/get-created-recipes/{id}")
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

    @PatchMapping(path = "/add-to-favorites")
    public ResponseEntity<UserResponse> addToFavorites(@RequestParam("userId") Long userId,
                                                       @RequestParam("recipeId") Long recipeId) {
        return new ResponseEntity<>(userService.addToFavorites(userId, recipeId), HttpStatus.OK);
    }

    @PatchMapping(path = "/remove-from-favorites")
    public ResponseEntity<UserResponse> removeFromFavorites(@RequestParam("userId") Long userId,
                                                            @RequestParam("recipeId") Long recipeId) {
        return new ResponseEntity<>(userService.removeFromFavorites(userId, recipeId), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}