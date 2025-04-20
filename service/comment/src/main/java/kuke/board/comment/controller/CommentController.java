package kuke.board.comment.controller;

import kuke.board.comment.service.CommentService;
import kuke.board.comment.service.Request.CommentCreateRequest;
import kuke.board.comment.service.Response.CommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;
    
    @GetMapping("/v1/comments/{commentId}")
    public CommentResponse read(
            @PathVariable(value = "commentId") Long commentId
    ) {
        return commentService.read(commentId);
    }
    
    @PostMapping("/v1/comments")
    public CommentResponse create(@RequestBody CommentCreateRequest request) {
        return commentService.create(request);
    }
    
    @DeleteMapping("/v1/comments/{commentId}")
    public void delete(@PathVariable(value = "commentId") Long commentId) {
        commentService.delete(commentId);
    } 
}
