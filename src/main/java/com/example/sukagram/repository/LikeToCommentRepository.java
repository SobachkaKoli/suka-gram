package com.example.sukagram.repository;

import com.example.sukagram.model.Comment;
import com.example.sukagram.model.LikeToComment;
import com.example.sukagram.model.LikeToPost;
import com.example.sukagram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeToCommentRepository extends MongoRepository<LikeToComment,String> {

    LikeToComment findByCommentId(String postId);
    void deleteByCommentIdAndAuthor(String postId, User author);
    boolean existsByCommentIdAndAuthor(String commentId, User author);

    int countLikeToCommentByComment(Comment comment);
}
