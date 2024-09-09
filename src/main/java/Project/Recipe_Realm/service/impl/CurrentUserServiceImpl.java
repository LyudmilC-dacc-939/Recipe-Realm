package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.model.User;
import Project.Recipe_Realm.repository.UserRepository;
import Project.Recipe_Realm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    public User extractCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return (User) principal;
        } else {
            return userService.findByEmail(principal.toString());
        }
    }

    public Boolean isCurrentUserMatch(User recipeOrCommentOwner) {
        User currentUser = extractCurrentUser();
        if (currentUser != null && recipeOrCommentOwner != null) {
            return recipeOrCommentOwner.getUsername().equals(extractCurrentUser().getUsername());
        }
        return false;
    }

    public boolean isCurrentUserARole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority authority : authentication.getAuthorities()) {
                if (authority.getAuthority().equals(role)) {
                    return true;
                }
            }
        }
        return false;
    }


}
