package com.example.sukagram.controller;

import com.example.sukagram.DTO.CommentDTO;
import com.example.sukagram.DTO.PostDTO;
import com.example.sukagram.Exception.*;
import com.example.sukagram.model.Comment;
import com.example.sukagram.model.LikeToPost;
import com.example.sukagram.model.Post;
import com.example.sukagram.model.SponsorPost;
import com.example.sukagram.service.CommentService;
import com.example.sukagram.service.LikeToPostService;
import com.example.sukagram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/user")
public class PostController {
    private final PostService postService;
    private final LikeToPostService likeToPostService;
    private final CommentService commentService;
    @Autowired
    public PostController(PostService postService, LikeToPostService likeToPostService, CommentService commentService) {
        this.postService = postService;
        this.likeToPostService = likeToPostService;
        this.commentService = commentService;
    }

    @GetMapping("/get-posts")
    private ResponseEntity<List<Post>> getPosts(){return ResponseEntity.ok().body(postService.findAll());}

    @GetMapping("/get-myPosts")
    private ResponseEntity<List<Post>> getPostsAuthenticatedUser(@RequestHeader("Authorization") String token){
        return ResponseEntity.ok().body(postService.getAllByAuthenticated(token));}

    @GetMapping("/get-user-posts/{userId}")
    private ResponseEntity<List<Post>> getPostsUser(@PathVariable String userId){return ResponseEntity.ok().body(
            postService.getAllByUserId(userId));}

    @GetMapping("/get-post-comments/{postId}")
    private ResponseEntity<List<Comment>> getPostComments(@PathVariable String postId){
        return ResponseEntity.ok().body(postService.getCommentsByPostId(postId));
    }

    @PostMapping("/createPost")
    private ResponseEntity<Post> createPost(@ModelAttribute PostDTO postDTO,@RequestHeader("Authorization") String token) throws IOException, Status443FileIsNullException {
        return ResponseEntity.ok().body(postService.createPost(postDTO,token));
    }
    @PostMapping("/create-sponsorr-Post/{sponsorId}")
    private ResponseEntity<SponsorPost> createSponsorPost(@ModelAttribute PostDTO postDTO,
                                                          @RequestHeader("Authorization") String token,
                                                          @PathVariable String sponsorId) throws IOException, Status443FileIsNullException, Status430UserNotFoundException {
        return ResponseEntity.ok().body(postService.createSponsorPost(postDTO,sponsorId,token));
    }

    @PatchMapping("/update-post/{postId}")
    public ResponseEntity<Post> updatePost(@ModelAttribute PostDTO postDTO,@PathVariable String postId,@RequestHeader("Authorization") String token)
            throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException {
        return ResponseEntity.ok().body(postService.updatePost(postDTO,postId,token));
    }
//    @GetMapping("/get-comments-post/{postId}")
//    private ResponseEntity<List<Comment>> getPostComments(@PathVariable String postId){
//        return ResponseEntity.ok().body(commentService.getPostComments(postId));
//    }


    @PostMapping("/post/set-like/{postId}")
    private ResponseEntity<LikeToPost> setLikeToPost(@PathVariable String postId, @RequestHeader("Authorization") String token)
            throws Status437LikeAlreadyExistsException, Status440PostNotFoundException {
        likeToPostService.setLikeToPost(postId,token);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/post/unset-like/{postId}")
    public ResponseEntity<LikeToPost> unsetLikeToPost(@PathVariable String postId, @RequestHeader("Authorization") String token) throws Status438LikeNotFoundException, Status440PostNotFoundException {
        likeToPostService.unSetLikeToPost(postId, token);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/post/addComment/{postId}")
    public ResponseEntity<Comment> addComment(@RequestBody CommentDTO commentDTO,
                                              @PathVariable String postId,
                                              @RequestHeader("Authorization") String token) throws Status440PostNotFoundException {
        commentService.addComment(commentDTO,postId,token);
        return new ResponseEntity<>(CREATED);
    }

    @DeleteMapping("/post/delete/{id}")
    private void deletePost(@PathVariable String id, @RequestHeader("Authorization") String token) throws Status435UserNotPostAuthorException, Status440PostNotFoundException {
        postService.deletePostById(id,token);
    }

}
