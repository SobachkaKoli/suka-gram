package com.example.sukagram.service;

import com.example.sukagram.Exception.Status443FileIsNullException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PictureService {

    String savePicture(MultipartFile multipartFile,String dir,String token) throws IOException, Status443FileIsNullException;

    String createPostPicturePath(String token);
    String createAvatarPath(String token);
}
