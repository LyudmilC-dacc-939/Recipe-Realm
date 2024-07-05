package Project.Recipe_Realm.service;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.dto.CommentResponse;

public interface CommentService {

    CommentResponse addComment(CommentRequest commentRequest);

    CommentResponse getAllCommentsForRecipe(Long id);

    void deleteComment(Long id);

    CommentResponse likeComment(Long id);

    CommentResponse dislikeComment(Long id);
}
