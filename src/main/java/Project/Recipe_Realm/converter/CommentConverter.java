package Project.Recipe_Realm.converter;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.service.impl.CurrentUserServiceImpl;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class CommentConverter {

    private final RecipeRepository recipeRepository;
    private final CurrentUserServiceImpl currentUserService;

    public CommentConverter(RecipeRepository recipeRepository,
                            CurrentUserServiceImpl currentUserService) {
        this.currentUserService = currentUserService;
        this.recipeRepository = recipeRepository;
    }

    public Comment toComment(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setCreatedAt(Instant.now());
        comment.setText(commentRequest.getText());
        comment.setLikes(0L);
        comment.setDislikes(0L);
        comment.setUser(currentUserService.extractCurrentUser());
        comment.setRecipe(recipeRepository.getReferenceById(commentRequest.getRecipeId()));
        return comment;
    }
}
