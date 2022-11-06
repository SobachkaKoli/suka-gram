package com.example.sukagram.controller;

import com.example.sukagram.service.PictureService;
import com.example.sukagram.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class PictureController {

    @Value("${upload.path}")
    private String uploadPath;
    private final PictureService pictureService;
    private final UserService userService;

    public PictureController(PictureService pictureService, UserService userService) {
        this.pictureService = pictureService;
        this.userService = userService;
    }

//    @PostMapping("/upload-avatar")
//    public ResponseEntity<Avatar> uploadAvatar(@RequestParam("file")MultipartFile file,
//                                               @RequestHeader("Authorization") String token) throws IOException {
//       return ResponseEntity.ok().body(userService.setAvatar(file,token));
//    }
}
