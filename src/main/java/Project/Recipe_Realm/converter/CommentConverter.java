package Project.Recipe_Realm.converter;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CommentConverter {

    private UserRepository userRepository;
    private RecipeRepository recipeRepository;

    public Comment toComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setCreatedAt(Instant.now());
        comment.setText(commentRequest.getText());
        comment.setUser(userRepository.getReferenceById(commentRequest.getUserId()));
        comment.setRecipe(recipeRepository.getReferenceById(commentRequest.getRecipeId()));
        return comment;
    }
}
