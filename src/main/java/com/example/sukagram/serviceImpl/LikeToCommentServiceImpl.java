package com.example.sukagram.serviceImpl;

import com.example.sukagram.Exception.Status437LikeAlreadyExistsException;
import com.example.sukagram.Exception.Status438LikeNotFoundException;
import com.example.sukagram.Exception.Status439CommentNotFoundException;
import com.example.sukagram.model.Comment;
import com.example.sukagram.model.LikeToComment;
import com.example.sukagram.repository.CommentRepository;
import com.example.sukagram.repository.LikeToCommentRepository;
import com.example.sukagram.service.CommentService;
import com.example.sukagram.service.LikeToCommentService;
import com.example.sukagram.service.NotificationService;
import com.example.sukagram.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class LikeToCommentServiceImpl implements LikeToCommentService {
    private final LikeToCommentRepository likeToCommentRepository;
    private final CommentService commentService;
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final NotificationService notificationService;

    public LikeToCommentServiceImpl(LikeToCommentRepository likeToCommentRepository, CommentService commentService, CommentRepository commentRepository, UserService userService, NotificationService notificationService) {
        this.likeToCommentRepository = likeToCommentRepository;
        this.commentService = commentService;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.notificationService = notificationService;

    }

    @Override
    public void setLikeToComment(String commentId, String token) throws Status437LikeAlreadyExistsException, Status439CommentNotFoundException {
        if(commentService.existsById(commentId)) {

            if (likeToCommentRepository.existsByCommentIdAndAuthor(commentId, userService.getAuthenticatedUser(token).get())) {
                throw new Status437LikeAlreadyExistsException("comment with id: " + commentId);
            } else {
                Comment comment = commentService.getById(commentId);
                LikeToComment likeToComment = LikeToComment.builder()
                        .comment(comment)
                        .author(userService.getAuthenticatedUser(token).get())
                        .build();
                likeToCommentRepository.save(likeToComment);
                notificationService.sendNotification(token,
                        commentService.getById(commentId).getAuthor().getId(),
                        "New like to the comment from " + userService.getAuthenticatedUser(token).get().getUserName());
                comment.setCountLikes(likeToCommentRepository.countLikeToCommentByComment(comment));
                commentRepository.save(comment);
            }
        }else {
            throw new Status439CommentNotFoundException(commentId);
        }
    }

    @Override
    public void unSetLikeToComment(String commentId, String token) throws Status438LikeNotFoundException,Status439CommentNotFoundException {
        if(commentService.existsById(commentId)) {
            if (likeToCommentRepository.existsByCommentIdAndAuthor(commentId, userService.getAuthenticatedUser(token).get())) {
                likeToCommentRepository.deleteByCommentIdAndAuthor(commentId, userService.getAuthenticatedUser(token).get());
                Comment comment = commentService.getById(commentId);
                comment.setCountLikes(likeToCommentRepository.countLikeToCommentByComment(comment));
                commentRepository.save(comment);
            } else {
                throw new Status438LikeNotFoundException(" comment with id : " + commentId);
            }
        }  else {
                throw new Status439CommentNotFoundException(commentId);
        }
    }
}
