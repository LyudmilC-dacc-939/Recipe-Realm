package Project.Recipe_Realm.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class RecipeRequest {
    @NotNull(message = "Recipe name cannot be null")
    private String title;
    @NotNull(message = "Recipe must have a description!")
    @Size(min = 20, message = "Description must be at least 20 characters!")
    private String description;
    @NotNull(message = "Recipe must have at least 1 ingredient")
    private String ingredients;
    @NotNull(message = "Recipe instructions cannot be null")
    private String instructions;
    @NotNull(message = "Recipe must have a category!")
    private String category;
    @NotNull(message = "UserId cannot be null")
    private Long userId;
}
