package com.example.sukagram.service;

import com.example.sukagram.Exception.Status437LikeAlreadyExistsException;
import com.example.sukagram.Exception.Status438LikeNotFoundException;
import com.example.sukagram.Exception.Status439CommentNotFoundException;

public interface LikeToCommentService {

    void setLikeToComment(String commentId, String token) throws Status437LikeAlreadyExistsException, Status439CommentNotFoundException;
    void unSetLikeToComment(String commentId,String token) throws Status438LikeNotFoundException, Status439CommentNotFoundException;

}
