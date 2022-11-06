package com.example.sukagram.service;

import com.example.sukagram.Exception.Status443FileIsNullException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface AvatarService {
    String setAvatar(MultipartFile file, String token) throws IOException, Status443FileIsNullException;
}
