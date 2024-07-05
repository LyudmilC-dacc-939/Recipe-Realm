package Project.Recipe_Realm.dto;

import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.model.User;
import lombok.Data;

import java.time.Instant;

@Data
public class CommentResponse {

    private Long id;
    private String text;
    private Instant createdAt;
    private Long likes;
    private Long dislikes;
    private User user;
    private Recipe recipe;
}
