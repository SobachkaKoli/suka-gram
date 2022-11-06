package com.example.sukagram.service;

import com.example.sukagram.DTO.CommentDTO;
import com.example.sukagram.Exception.Status436UserNotCommentAuthorException;
import com.example.sukagram.Exception.Status439CommentNotFoundException;
import com.example.sukagram.Exception.Status440PostNotFoundException;
import com.example.sukagram.model.Comment;

import java.util.List;

public interface CommentService {


    void addComment(CommentDTO commentDTO, String postId,String token) throws Status440PostNotFoundException;

    void deleteComment(String commentId, String token) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException;

    boolean userIsCommentAuthor(String commentId, String token);

    boolean existsById(String id);

//    List<Comment> getPostComments(String posId);
    void deleteAllByPostId(String postId);
    Comment getById(String commentId);
}
