package com.example.sukagram.serviceImpl;

import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.model.Role;
import com.example.sukagram.model.User;
import com.example.sukagram.repository.UserRepository;
import com.example.sukagram.service.AdminService;
import com.example.sukagram.service.CommentService;
import com.example.sukagram.service.PostService;
import com.example.sukagram.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    private final UserRepository userRepository;


    public AdminServiceImpl(UserService userService, PostService postService, CommentService commentService, UserRepository userRepository) {
        this.userService = userService;
        this.postService = postService;
        this.commentService = commentService;
        this.userRepository = userRepository;
    }


    @Override
    public void deleteUserById(String userId) {

    }

    @Override
    public void deletePostById(String postId) {

    }

    @Override
    public void deleteCommentById(String commentId) {
    }

    @Override
    public User banUserById(String userId) throws Status430UserNotFoundException {
        if (!userService.existsById(userId)){
            throw new Status430UserNotFoundException(userId);
        }else {
            Optional<User> user = userService.getById(userId);
            user.get().setRole(Role.BANNED_USER);
           return userRepository.save(user.get());
        }
    }

    @Override
    public User unBanUserById(String userId) throws Status430UserNotFoundException {
        if (!userService.existsById(userId)){
            throw new Status430UserNotFoundException(userId);
        }else {
            Optional<User> user = userService.getById(userId);
            user.get().setRole(Role.USER);
            return userRepository.save(user.get());
        }
    }
}
