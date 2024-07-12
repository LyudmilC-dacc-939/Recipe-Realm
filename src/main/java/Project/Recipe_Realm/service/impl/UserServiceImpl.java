package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.advice.exception.RecordAlreadyExistsException;
import Project.Recipe_Realm.advice.exception.RecordNotFoundException;
import Project.Recipe_Realm.converter.UserConverter;
import Project.Recipe_Realm.dto.LoginRequest;
import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.repository.UserRepository;
import Project.Recipe_Realm.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserConverter userConverter;
    private RecipeRepository recipeRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter,
                           RecipeRepository recipeRepository) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEMail()).isPresent()) {
            throw new RecordAlreadyExistsException("Email is taken!");
        }
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent()) {
            throw new RecordAlreadyExistsException("Username is taken!");
        }

        User user = userConverter.toUser(userRequest);
        userRepository.save(user);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        System.out.println(HttpStatus.CREATED);

        return userResponse;
    }

    @Override
    public String login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", id)));

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(user, userResponse);
        System.out.println(HttpStatus.FOUND);
        return userResponse;
    }

    @Override
    public User updateUser(UserRequest userRequest, Long id) {
        User existingUser = userRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", id)));
        Optional<User> alreadyTakenEmail = userRepository.findByEmail(userRequest.getEMail());
        if (alreadyTakenEmail.isPresent()) {
            throw new RecordAlreadyExistsException(String.format("Email %s is already taken!", userRequest.getEMail()));
        }
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEMail());
        existingUser.setPassword(userRequest.getPassword());
        existingUser.setProfilePicture(userRequest.getProfilePicture());

        System.out.println(HttpStatus.ACCEPTED);
        userRepository.save(existingUser);
        return existingUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException("Invalid Id, Deletion aborted"));
        userRepository.deleteById(id);
    }

    @Override
    public Set<Recipe> getAllRecipesForUser(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", id)));
        return userRepository.getReferenceById(id).getRecipes();
    }

    @Override
    public Set<Recipe> getAllFavoriteRecipesForUser(Long id) {
        userRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", id)));
        return userRepository.getReferenceById(id).getFavorites();
    }

    @Override
    public UserResponse addToFavorites(Long userId, Long recipeId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", userId)));
        Recipe existingRecipe = recipeRepository.findById(recipeId).orElseThrow(()->
                new RecordNotFoundException(String.format("Recipe with id %s not exist", recipeId)));
        existingUser.getRecipes().add(existingRecipe);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(existingUser, userResponse);
        return userResponse;
    }

    @Override
    public UserResponse removeFromFavorites(Long userId, Long recipeId) {
        User existingUser = userRepository.findById(userId).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", userId)));
        Recipe existingRecipe = recipeRepository.findById(recipeId).orElseThrow(()->
                new RecordNotFoundException(String.format("Recipe with id %s not exist", recipeId)));
        existingUser.getRecipes().remove(existingRecipe);

        UserResponse userResponse = new UserResponse();
        BeanUtils.copyProperties(existingUser, userResponse);
        return userResponse;
    }

}
