package com.example.sukagram.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.sukagram.DTO.UserDTO;
import com.example.sukagram.Exception.Status434UserNicknameNotUniqueException;
import com.example.sukagram.Exception.Status443FileIsNullException;
import com.example.sukagram.model.User;
import com.example.sukagram.model.Role;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(UserDTO userDTO) throws Status434UserNicknameNotUniqueException;
//    Role saveRole(Role role);
//    void addRoleToUser(String roleName, String userName);

    Optional<User> getUser(String userName);

    Optional<User> getById(String userId);
    Optional<User> getAuthenticatedUser(String token);


    List<User> getUsers();

    boolean existsById(String userId);
//    Optional<User> findAuthenticatedUser();

    DecodedJWT decodedJWT(String token);


}
