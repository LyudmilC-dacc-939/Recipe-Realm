package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.dto.CommentResponse;
import Project.Recipe_Realm.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public CommentResponse addComment(CommentRequest commentRequest) {
        return null;
    }

    @Override
    public CommentResponse getAllCommentsForRecipe(Long id) {
        return null;
    }

    @Override
    public void deleteComment(Long id) {

    }

    @Override
    public CommentResponse likeComment(Long id) {
        return null;
    }

    @Override
    public CommentResponse dislikeComment(Long id) {
        return null;
    }
}
