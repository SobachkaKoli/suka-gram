package com.example.sukagram.service;

import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.model.User;

public interface AdminService {

    void deleteUserById(String userId);
    void deletePostById(String  postId);
    void deleteCommentById(String commentId);
    User banUserById(String userId) throws Status430UserNotFoundException;

    User unBanUserById(String userId) throws Status430UserNotFoundException;


}
