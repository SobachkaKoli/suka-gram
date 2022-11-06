package com.example.sukagram.repository;

import com.example.sukagram.model.LikeToPost;
import com.example.sukagram.model.Post;
import com.example.sukagram.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeToPostRepository extends MongoRepository<LikeToPost,String> {

    LikeToPost findByPostId(String postId);
    boolean existsLikePostByPostId(String postId);
    boolean existsByAuthor(User author);

    LikeToPost findByPostIdAndAuthor(String postId, User author);
    void deleteByPostIdAndAuthor(String postId,User author);
    boolean existsByPostIdAndAuthor(String postId, User author);

    int countLikeToPostByPost(Post post);
}
