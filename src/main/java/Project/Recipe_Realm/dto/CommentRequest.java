package Project.Recipe_Realm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {
    @NotNull(message = "Comment cannot be empty")
    @Size(min = 5, message = "Comment must contain at least 5 characters.")
    @Size(max = 400, message = "Comment can't be more than 400 characters.")
    private String text;
    @NotNull(message = "Comment must be from a valid user!")
    private Long userId;
    @NotNull(message = "Recipe id should be valid")
    private Long recipeId;
}
