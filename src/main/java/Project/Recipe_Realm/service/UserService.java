package Project.Recipe_Realm.service;

import Project.Recipe_Realm.dto.LoginRequest;
import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;

import java.util.Set;


public interface UserService {

    UserResponse createUser (UserRequest userRequest);

    String login (LoginRequest loginRequest);

    UserResponse getUserById(Long id);

    User updateUser(UserRequest userRequest, Long id);

    void deleteUser(Long id);

    Set<Recipe> getAllRecipesForUser (Long id);

    Set<Recipe> getAllFavoriteRecipesForUser(Long id);

    UserResponse addToFavorites(Long userId, Long recipeId);

    UserResponse removeFromFavorites(Long userId, Long recipeId);

    User findByEmail(String username);
    
}
