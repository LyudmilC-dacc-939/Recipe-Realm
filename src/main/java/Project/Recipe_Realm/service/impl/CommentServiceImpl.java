package Project.Recipe_Realm.service.impl;

import Project.Recipe_Realm.advice.exception.RecordNotFoundException;
import Project.Recipe_Realm.converter.CommentConverter;
import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.dto.CommentResponse;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.model.Recipe;
import Project.Recipe_Realm.repository.CommentRepository;
import Project.Recipe_Realm.repository.RecipeRepository;
import Project.Recipe_Realm.repository.UserRepository;
import Project.Recipe_Realm.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentConverter commentConverter;
    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CurrentUserServiceImpl currentUserService;


    @Override
    public CommentResponse addComment(CommentRequest commentRequest) {
        Comment newComment = commentConverter.toComment(commentRequest);
        commentRepository.save(newComment);

        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(newComment, commentResponse);
        System.out.println(HttpStatus.CREATED);
        return commentResponse;
    }

    @Override
    public Set<Comment> getAllCommentsForRecipe(Long id) {
        Recipe currentRecipe = recipeRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("Recipe with ID: %s not exist", id)));
        System.out.println(HttpStatus.FOUND);
        return currentRecipe.getComments();
    }

    @Override
    public void deleteComment(Long id) {
       Comment comment = commentRepository.findById(id).orElseThrow(() ->
                new RecordNotFoundException(String.format("Comment with ID: %s not exist", id)));
        boolean canDelete = currentUserService.isCurrentUserMatch(comment.getUser());
        canDelete |= currentUserService.isCurrentUserARole("ROLE_MODERATOR");
        if (!canDelete) {
            throw new RecordNotFoundException("You cannot delete foreign comments");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public CommentResponse likeComment(Long commentId, Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", userId)));
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new RecordNotFoundException(String.format("Comment with id %s not exist", commentId)));
        if (existingComment.getUsersIdDisliked().contains(userId)) {
            existingComment.getUsersIdDisliked().remove(userId);
            existingComment.setDislikes(existingComment.getDislikes() - 1);
        }
        existingComment.getUsersIdLiked().add(userId);
        existingComment.setLikes(existingComment.getLikes() + 1);
        commentRepository.save(existingComment);

        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(existingComment, commentResponse);
        System.out.println(commentResponse);
        System.out.println(HttpStatus.ACCEPTED);
        return commentResponse;
    }

    @Override
    public CommentResponse dislikeComment(Long commentId, Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new RecordNotFoundException(String.format("User with id %s not exist", userId)));
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() ->
                new RecordNotFoundException(String.format("Comment with id %s not exist", commentId)));
        if (existingComment.getUsersIdLiked().contains(userId)) {
            existingComment.getUsersIdLiked().remove(userId);
            existingComment.setLikes(existingComment.getLikes() - 1);
        }
        existingComment.getUsersIdDisliked().add(userId);
        existingComment.setDislikes(existingComment.getDislikes() + 1);
        System.out.println(HttpStatus.ACCEPTED);
        commentRepository.save(existingComment);

        CommentResponse commentResponse = new CommentResponse();
        BeanUtils.copyProperties(existingComment, commentResponse);
        System.out.println(commentResponse);
        return commentResponse;
    }
}
