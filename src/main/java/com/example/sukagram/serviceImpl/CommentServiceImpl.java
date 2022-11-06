package com.example.sukagram.serviceImpl;

import com.example.sukagram.DTO.CommentDTO;
import com.example.sukagram.Exception.Status436UserNotCommentAuthorException;
import com.example.sukagram.Exception.Status439CommentNotFoundException;
import com.example.sukagram.Exception.Status440PostNotFoundException;
import com.example.sukagram.notifications.NotificationServiceImpl;
import com.example.sukagram.model.Comment;
import com.example.sukagram.model.Post;
import com.example.sukagram.repository.CommentRepository;
import com.example.sukagram.repository.PostRepository;
import com.example.sukagram.service.CommentService;
import com.example.sukagram.service.NotificationService;
import com.example.sukagram.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public CommentServiceImpl( PostRepository postRepository, UserService userService, CommentRepository commentRepository, NotificationServiceImpl notificationServiceImpl, NotificationService notificationService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.notificationService = notificationService;
    }


    @Override
    public void addComment(CommentDTO commentDTO, String postId,String token) throws Status440PostNotFoundException {
        if(postRepository.existsById(postId)) {
            Post post = postRepository.getPostById(postId);
            Comment comment = Comment.builder()
                    .text(commentDTO.getText())
                    .author(userService.getAuthenticatedUser(token).get())
                    .post(post)
                    .build();
            post.getComments().add(comment);
            commentRepository.save(comment);
            notificationService.sendNotification(token,
                    post.getAuthor().getId(),
                    "New comment to the post from " + userService.getAuthenticatedUser(token).get().getUserName());
            post.setCountComment(post.getCountComment() + 1);
            postRepository.save(post);
        }else{
            throw new Status440PostNotFoundException(postId);
        }
    }

    @Override
    public void deleteComment(String commentId, String token) throws Status436UserNotCommentAuthorException, Status439CommentNotFoundException {
        if (commentRepository.existsById(commentId)) {
            if (userIsCommentAuthor(commentId,token)) {
                commentRepository.deleteById(commentId);
            } else {
                throw new Status436UserNotCommentAuthorException(commentId);
            }
        }else {
            throw new Status439CommentNotFoundException(commentId);
        }
    }

    @Override
    public boolean userIsCommentAuthor(String commentId, String token) {
        String userId = userService.getAuthenticatedUser(token).get().getId();
        String authorId = commentRepository.findById(commentId).get().getAuthor().getId();
        return authorId.equals(userId);
    }

    @Override
    public boolean existsById(String commentId) {
        return commentRepository.existsById( commentId);
    }

//    @Override
//    public List<Comment> getPostComments(String postId) {
//        return commentRepository.getCommentsByPostId(postId);
//    }

    @Override
    public void deleteAllByPostId(String postId) {
        commentRepository.deleteAllByPostId(postId);
    }
    @Override
    public Comment getById(String commentId) {
        return commentRepository.getCommentById(commentId);
    }
}
