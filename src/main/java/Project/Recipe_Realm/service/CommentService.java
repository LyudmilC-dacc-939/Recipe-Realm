package Project.Recipe_Realm.service;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.dto.CommentResponse;
import Project.Recipe_Realm.model.Comment;

import java.util.Set;

public interface CommentService {

    CommentResponse addComment(CommentRequest commentRequest);

    Set<Comment> getAllCommentsForRecipe(Long id);

    void deleteComment(Long id);

    CommentResponse likeComment(Long commentId, Long userId);

    CommentResponse dislikeComment(Long commentId, Long userId);
}
