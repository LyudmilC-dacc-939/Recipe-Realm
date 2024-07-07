package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.dto.LoginRequest;
import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.dto.UserResponse;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.repository.UserRepository;
import Project.Recipe_Realm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public abstract class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        return null;
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

    }

    @Override
    public Set<Recipe> getAllRecipesForUser(Long id) {
        return null;
    }

    @Override
    public Set<Recipe> getAllFavoriteRecipesForUser(Long id) {
        return null;
    }
}
