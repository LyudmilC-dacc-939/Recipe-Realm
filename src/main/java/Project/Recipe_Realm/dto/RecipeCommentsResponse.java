package Project.Recipe_Realm.dto;

import Project.Recipe_Realm.model.Comment;
import lombok.Data;

import java.util.Set;

@Data
public class RecipeCommentsResponse {
    private Long id;
    private String title;
    private Long likes;
    private Long dislikes;
    private Set<Comment> comments;
}