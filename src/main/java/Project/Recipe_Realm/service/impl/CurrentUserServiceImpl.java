package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl {
    @Autowired
    private UserRepository userRepository;

    public User extractCurrentUser(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal instanceof UserDetails){
            return (User) principal;
        }else{
            return userRepository.getByEmail(principal.toString());
        }
    }

    public Boolean isCurrentUserRecipeOwner(User recipeOwner){
        User currentUser = extractCurrentUser();
        if(currentUser != null && recipeOwner != null){
            return recipeOwner.getEmail().equals(extractCurrentUser().getEmail());
        }
        return false;
    }


}
