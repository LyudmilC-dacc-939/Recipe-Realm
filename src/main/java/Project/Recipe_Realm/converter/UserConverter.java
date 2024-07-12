package Project.Recipe_Realm.converter;

import Project.Recipe_Realm.dto.UserRequest;
import Project.Recipe_Realm.enums.Role;
import Project.Recipe_Realm.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final PasswordEncoder passwordEncoder;

    public User toUser(UserRequest userRequest) {
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEMail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setProfilePicture(userRequest.getProfilePicture());
        user.setCreatedAt(Instant.now());
        user.setRole(Role.USER);
        return user;
    }
}
