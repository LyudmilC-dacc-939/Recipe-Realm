package Project.Recipe_Realm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class UserRequest {
    @NotNull(message = "Username cannot be null")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    private String username;
    @NotNull(message = "Password cannot be null")
    private String password;
    @NotNull(message = "Email cannot be null")
    @Size(min = 9, max = 50, message = "Email must be at least 9 characters and at most 50 characters")
    @Email
    private String eMail;
    @URL
    private String profilePicture;
}
