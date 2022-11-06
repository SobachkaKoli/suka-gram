package com.example.sukagram.serviceImpl;

import com.example.sukagram.DTO.PostDTO;
import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.Exception.Status435UserNotPostAuthorException;
import com.example.sukagram.Exception.Status440PostNotFoundException;
import com.example.sukagram.Exception.Status443FileIsNullException;
import com.example.sukagram.model.Comment;
import com.example.sukagram.model.SponsorPost;
import com.example.sukagram.model.User;
import com.example.sukagram.model.Post;
import com.example.sukagram.repository.PostRepository;
import com.example.sukagram.repository.SponsorPostRepository;
import com.example.sukagram.service.CommentService;
import com.example.sukagram.service.PictureService;
import com.example.sukagram.service.PostService;
import com.example.sukagram.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final CommentService commentService;
    private final SponsorPostRepository sponsorPostRepository;
    private final PictureService pictureService;


    public PostServiceImpl(PostRepository postRepository, UserService userService, CommentService commentService, SponsorPostRepository sponsorPostRepository, PictureService pictureService) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.commentService = commentService;

        this.sponsorPostRepository = sponsorPostRepository;
        this.pictureService = pictureService;
    }
    //TODO винести в окремий метод сохранение картинок
    @Override
    public Post createPost(PostDTO postDTO,String token) throws IOException, Status443FileIsNullException {

        List<String> picturePath = new ArrayList<>();
        for (MultipartFile file : postDTO.getPictures()){
                picturePath.add(pictureService.savePicture(
                        file,pictureService.createPostPicturePath(token),token));
        }
        return postRepository.save(Post.builder()
                .author(userService.getAuthenticatedUser(token).get())
                .text(postDTO.getText())
                .picturePath(picturePath)
                .comments(new ArrayList<>())
                .build());
    }

    @Override
    public SponsorPost createSponsorPost(PostDTO postDTO,String sponsorId, String token) throws IOException, Status443FileIsNullException, Status430UserNotFoundException {
        if (userService.existsById(sponsorId)){
            return sponsorPostRepository.save(SponsorPost.builder()
                    .post(createPost(postDTO,token))
                    .sponsor(userService.getById(sponsorId).get())
                    .build());
        }else {
            throw new Status430UserNotFoundException(sponsorId);
        }
    }

    @Override
    public Post getPostById(String id) {
        return postRepository.getPostById(id);
    }

    @Override
    public void deletePostById(String postId, String token) throws Status435UserNotPostAuthorException, Status440PostNotFoundException {
        if(postRepository.existsById(postId)) {
            if (userIsPostAuthor(postId, token)) {
                Optional<Post> post = postRepository.findById(postId);
                for (String picturePath : post.get().getPicturePath()){
                    File picture = new File(picturePath);
                    picture.delete();
                }
                commentService.deleteAllByPostId(postId);
                postRepository.deleteById(postId);
            } else {
                throw new Status435UserNotPostAuthorException(postId);
            }
        }else {
            throw new Status440PostNotFoundException(postId);
        }
    }

    @Override
    public Post updatePost(PostDTO postDTO, String postId, String token) throws Status435UserNotPostAuthorException, Status440PostNotFoundException, IOException, Status443FileIsNullException {
        if(!postRepository.existsById(postId)){
            throw new Status440PostNotFoundException(postId);
        }else {
            Post post = postRepository.findById(postId).orElseThrow();
            if (userIsPostAuthor(postId, token)) {
                for (String picturePath : post.getPicturePath()){
                    File picture = new File(picturePath);
                    picture.delete();
                }
                post.getPicturePath().clear();
                for (MultipartFile picture : postDTO.getPictures()){
                       post.getPicturePath().add(pictureService.savePicture(picture,pictureService.createPostPicturePath(token),token));
                }
                post.setText(postDTO.getText());
                return postRepository.save(post);
            } else {
                throw new Status435UserNotPostAuthorException(postId);
            }
        }
    }

    @Override
    public boolean userIsPostAuthor(String postId, String token) {
        String userId = userService.getAuthenticatedUser(token).get().getId();
        String authorId = postRepository.findById(postId).get().getAuthor().getId();
        return authorId.equals(userId);
    }

    @Override
    public List<Post> getAllByAuthenticated(String token) {
        return postRepository.findAllByAuthor(userService.getAuthenticatedUser(token).get());
    }
    @Override
    public List<Comment> getCommentsByPostId(String postId) {
        return postRepository.getPostById(postId).getComments();
    }

    @Override
    public boolean existsById(String postId) {
        return postRepository.existsById(postId);
    }

    @Override
    public List<Post> getAllByUserId(String authorId) {
        return postRepository.findAllByAuthorId(authorId);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findAll(User user, String token) {

        return postRepository.findAllByAuthor(userService.getAuthenticatedUser(token).get());
    }

    @Override
    public List<Post> findRecommendations(User user,String token) {
        return findAll(user,token).stream()
                .flatMap(post -> post.getComments().stream())
                .map(Comment::getAuthor)
                .flatMap(commentAuthor -> findAll(commentAuthor,token).stream().limit(5))
                .collect(Collectors.toList());
    }


}
