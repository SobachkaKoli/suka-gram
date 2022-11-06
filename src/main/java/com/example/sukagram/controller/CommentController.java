package com.example.sukagram.controller;

import com.example.sukagram.Exception.Status436UserNotCommentAuthorException;
import com.example.sukagram.Exception.Status437LikeAlreadyExistsException;
import com.example.sukagram.Exception.Status438LikeNotFoundException;
import com.example.sukagram.Exception.Status439CommentNotFoundException;
import com.example.sukagram.model.LikeToPost;
import com.example.sukagram.service.CommentService;
import com.example.sukagram.service.LikeToCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
public class CommentController {

    private final CommentService commentService;
    private final LikeToCommentService likeToCommentService;
    @Autowired
    public CommentController(CommentService commentService, LikeToCommentService likeToCommentService) {
        this.commentService = commentService;
        this.likeToCommentService = likeToCommentService;
    }

    @DeleteMapping("/comment/delete/{commentId}")
    public void deleteComment(@PathVariable String commentId, @RequestHeader("Authorization") String token) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException {
        commentService.deleteComment(commentId, token);
    }
    
    @PostMapping("/comment/set-like/{commentId}")
    public void setLikeToComment(@PathVariable String commentId, @RequestHeader("Authorization") String token) throws Status437LikeAlreadyExistsException, Status439CommentNotFoundException {
        likeToCommentService.setLikeToComment(commentId,token);
    }

    @DeleteMapping("/comment/unset-like/{commentId}")
    public ResponseEntity<LikeToPost> unsetLikeToComment(@PathVariable String commentId, @RequestHeader("Authorization") String token) throws Status438LikeNotFoundException,Status439CommentNotFoundException {
        likeToCommentService.unSetLikeToComment(commentId, token);
        return new ResponseEntity<>(OK);
    }

}
