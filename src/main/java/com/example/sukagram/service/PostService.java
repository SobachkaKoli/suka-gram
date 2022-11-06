package com.example.sukagram.service;

import com.example.sukagram.DTO.PostDTO;
import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.Exception.Status435UserNotPostAuthorException;
import com.example.sukagram.Exception.Status440PostNotFoundException;
import com.example.sukagram.Exception.Status443FileIsNullException;
import com.example.sukagram.model.Comment;
import com.example.sukagram.model.Post;
import com.example.sukagram.model.SponsorPost;
import com.example.sukagram.model.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    Post createPost(PostDTO postDTO,String token) throws IOException, Status443FileIsNullException;

    SponsorPost createSponsorPost(PostDTO postDTO, String sponsorId, String token) throws IOException, Status443FileIsNullException, Status430UserNotFoundException;
    Post getPostById(String postId);
    void deletePostById(String postId,String token) throws Status435UserNotPostAuthorException, Status440PostNotFoundException;

    Post updatePost(PostDTO postDTO, String postId, String token) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException;
    boolean userIsPostAuthor(String postId, String token);

    List<Post> getAllByAuthenticated(String token);

    List<Comment> getCommentsByPostId(String postId);
    boolean existsById(String postId);


    List<Post> getAllByUserId(String userId);
    List<Post> findAll();
    List<Post> findAll(User user, String token);
    List<Post> findRecommendations(User user,String token);
}
