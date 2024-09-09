package Project.Recipe_Realm.controller;

import Project.Recipe_Realm.dto.CommentRequest;
import Project.Recipe_Realm.dto.CommentResponse;
import Project.Recipe_Realm.model.Comment;
import Project.Recipe_Realm.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Adds a new comment", description = "Creates a new comment for a recipe")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment successfully added",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PostMapping
    public ResponseEntity<CommentResponse> addComment(@Valid @RequestBody CommentRequest commentRequest) {
        return new ResponseEntity<>(commentService.addComment(commentRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Gets all comments for a recipe", description = "Retrieves all comments for the given recipe ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Recipe not found")
    })
    @GetMapping(path = "/comments-for-recipe/{id}")
    public ResponseEntity<Set<Comment>> getAllCommentsForRecipe(@PathVariable("id") Long id) {
        return new ResponseEntity<>(commentService.getAllCommentsForRecipe(id), HttpStatus.FOUND);
    }

    @Operation(summary = "Likes a comment", description = "Adds a like to a specific comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comment liked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment or user not found")
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PutMapping(path = "/like")
    public ResponseEntity<CommentResponse> likeComment(@RequestParam("commentId") Long commentId,
                                                       @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(commentService.likeComment(commentId, userId), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Dislikes a comment", description = "Adds a dislike to a specific comment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comment disliked successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class))),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment or user not found")
    })
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_MODERATOR')")
    @PutMapping(path = "/dislike")
    public ResponseEntity<CommentResponse> dislikeComment(@RequestParam("commentId") Long commentId,
                                                          @RequestParam("userId") Long userId) {
        return new ResponseEntity<>(commentService.dislikeComment(commentId, userId), HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Deletes a comment", description = "Deletes a comment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
