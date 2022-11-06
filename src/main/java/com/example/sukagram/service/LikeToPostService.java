package com.example.sukagram.service;

import com.example.sukagram.Exception.Status437LikeAlreadyExistsException;
import com.example.sukagram.Exception.Status438LikeNotFoundException;
import com.example.sukagram.Exception.Status440PostNotFoundException;

public interface LikeToPostService {

    void setLikeToPost(String postId, String token) throws Status437LikeAlreadyExistsException, Status440PostNotFoundException;

    void unSetLikeToPost(String postId, String token) throws Status438LikeNotFoundException, Status440PostNotFoundException;

}
