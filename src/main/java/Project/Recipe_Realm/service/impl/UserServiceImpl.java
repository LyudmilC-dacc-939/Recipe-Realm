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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public abstract class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserConverter userConverter;
    private RecipeRepository recipeRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserConverter userConverter) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
       if(userRepository.findByEMail(userRequest.getEMail()).isPresent()){
           throw new RecordAlreadyExistsException("Email is taken!");
       }
       if (userRepository.findByUsername(userRequest.getUsername()).isPresent()){
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
        return null;
    }

    @Override
    public User updateUser(UserRequest userRequest, Long id) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.findById(id).orElseThrow(()->
                new RecordNotFoundException("Invalid Id, Deletion aborted"));
        userRepository.deleteById(id);
    }

    @Override
    public Set<Recipe> getAllRecipesForUser(Long id) {
        return null;
    }

    @Override
    public Set<Recipe> getAllFavoriteRecipesForUser(Long id) {
        return null;
    }

    @Override
    public UserResponse addToFavorites(Long userId, Long recipeId) {
        return null;
    }

    @Override
    public UserResponse removeFromFavorites(Long userId, Long recipeId) {
        return null;
    }
}
