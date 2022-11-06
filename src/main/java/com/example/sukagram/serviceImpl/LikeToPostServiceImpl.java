package com.example.sukagram.serviceImpl;

import com.example.sukagram.Exception.Status437LikeAlreadyExistsException;
import com.example.sukagram.Exception.Status438LikeNotFoundException;
import com.example.sukagram.Exception.Status440PostNotFoundException;
import com.example.sukagram.model.LikeToPost;
import com.example.sukagram.model.Post;
import com.example.sukagram.repository.LikeToPostRepository;
import com.example.sukagram.repository.PostRepository;
import com.example.sukagram.service.LikeToPostService;
import com.example.sukagram.service.NotificationService;
import com.example.sukagram.service.PostService;
import com.example.sukagram.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class LikeToPostServiceImpl implements LikeToPostService {

    private final LikeToPostRepository likeToPostRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;
    private final PostRepository postRepository;
    public LikeToPostServiceImpl(UserService userService, PostService postService, LikeToPostRepository likeToPostRepository, NotificationService notificationService, PostRepository postRepository) {
        this.userService = userService;
        this.likeToPostRepository = likeToPostRepository;
        this.postService = postService;
        this.notificationService = notificationService;
        this.postRepository = postRepository;
    }

    @Override
    public void setLikeToPost(String postId, String token) throws Status437LikeAlreadyExistsException, Status440PostNotFoundException {
        if (!postService.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        }else {
            if (likeToPostRepository.existsByPostIdAndAuthor(postId, userService.getAuthenticatedUser(token).get())) {
                throw new Status437LikeAlreadyExistsException("you already liked post with id: " + postId);
            } else {
                Post post = postService.getPostById(postId);
                LikeToPost likeToPost = LikeToPost.builder()
                        .post(post)
                        .author(userService.getAuthenticatedUser(token).get())
                        .build();
                likeToPostRepository.save(likeToPost);
                notificationService.sendNotification(token,
                        postService.getPostById(postId).getAuthor().getId(),
                        "New like to the post from " + userService.getAuthenticatedUser(token).get().getUserName());

                post.setCountLikes(likeToPostRepository.countLikeToPostByPost(post));
                postRepository.save(post);
            }
        }
    }

    @Override
    public void unSetLikeToPost(String postId, String token) throws Status438LikeNotFoundException, Status440PostNotFoundException {
        if (!postService.existsById(postId)) {
            throw new Status440PostNotFoundException(postId);
        } else {
            if (likeToPostRepository.existsByPostIdAndAuthor(postId, userService.getAuthenticatedUser(token).get())) {
                likeToPostRepository.deleteByPostIdAndAuthor(postId, userService.getAuthenticatedUser(token).get());
                Post post = postService.getPostById(postId);
                post.setCountLikes(likeToPostRepository.countLikeToPostByPost(post));
                postRepository.save(post);
            } else {
                throw new Status438LikeNotFoundException("post with id :" + postId);
            }
        }
    }
}
