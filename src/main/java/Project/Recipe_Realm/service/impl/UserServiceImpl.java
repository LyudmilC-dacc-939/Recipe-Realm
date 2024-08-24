package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.advice.exception.RecordAlreadyExistsException;
import Project.Recipe_Realm.advice.exception.RecordNotFoundException;
import Project.Recipe_Realm.config.JwtService;
import Project.Recipe_Realm.converter.UserConverter;
import Project.Recipe_Realm.dto.LoginRequest;
import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.repository.UserRepository;
import Project.Recipe_Realm.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private CurrentUserServiceImpl currentUserService;


    @Override
    public UserResponse createUser(UserRequest userRequest) {
        if (userRepository.findByEmail(userRequest.getEmail()).isPresent()) {
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
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        ));

        var user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RecordNotFoundException("User not found or wrong password"));
        return jwtService.generateJwtToken(user);
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
        Optional<User> alreadyTakenEmail = userRepository.findByEmail(userRequest.getEmail());
        if (alreadyTakenEmail.isPresent()) {
            throw new RecordAlreadyExistsException(String.format("Email %s is already taken!", userRequest.getEmail()));
        }
        existingUser.setUsername(userRequest.getUsername());
        existingUser.setEmail(userRequest.getEmail());
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

    @Override
    public User findByEmail(String username) {
        return null;
    }

}
