package Project.Recipe_Realm.dto;

import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.model.User;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
public class RecipeResponse {

    private Long id;
    private String title;
    private String description;
    private String ingredients;
    private String instructions;
    private String category;
    private Instant createdAt;
    private Long likes;
    private Long dislikes;
    private User user;
    private Set<Comment> comments;
    private Set<User> usersFavorite;
}
