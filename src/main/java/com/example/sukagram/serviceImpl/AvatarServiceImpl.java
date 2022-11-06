package com.example.sukagram.serviceImpl;


import com.example.sukagram.Exception.Status443FileIsNullException;
import com.example.sukagram.model.User;
import com.example.sukagram.repository.UserRepository;
import com.example.sukagram.service.AvatarService;
import com.example.sukagram.service.PictureService;
import com.example.sukagram.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Value("${upload.path}")
    private String uploadPath;

    private final UserService userService;
    private final UserRepository userRepository;
    private final PictureService pictureService;

    public AvatarServiceImpl(UserService userService, UserRepository userRepository, PictureService pictureService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.pictureService = pictureService;
    }

    @Override
    public String setAvatar(MultipartFile file, String token) throws IOException, Status443FileIsNullException {

        String picturePath = pictureService.savePicture(
                file,pictureService.createAvatarPath(token),token);

        Optional<User> user = userRepository.findByUserName(userService.getAuthenticatedUser(token).get().getUserName());
        user.get().setAvatar(picturePath);
        userRepository.save(user.get());
        return picturePath;
    }
}
