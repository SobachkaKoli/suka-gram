package com.example.sukagram.repository;

import com.example.sukagram.model.Comment;
import com.example.sukagram.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {

    Comment getCommentById(String id);

    void deleteAllByPostId(String postId);

//    List<Comment> getCommentsByPostId(String postId);

}
