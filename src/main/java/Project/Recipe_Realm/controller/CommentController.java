package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.dto.CommentResponse;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest) {
        return new ResponseEntity<>(commentService.addComment(commentRequest), HttpStatus.CREATED);
    }

    @GetMapping(path = "/comments-for-recipe/{id}")
    public ResponseEntity<Set<Comment>> getAllCommentsForRecipe(@PathVariable("id") Long id) {
        return new ResponseEntity<>(commentService.getAllCommentsForRecipe(id), HttpStatus.FOUND);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
    @PutMapping(path = "/like")
    public ResponseEntity<CommentResponse> likeComment(@RequestParam("commentId") Long commentId,
                                                       @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(commentService.likeComment(commentId, userId), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MODERATOR')")
    @PutMapping(path = "/dislike")
    public ResponseEntity<CommentResponse> dislikeComment(@RequestParam("commentId") Long commentId,
                                                          @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(commentService.dislikeComment(commentId, userId), HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
