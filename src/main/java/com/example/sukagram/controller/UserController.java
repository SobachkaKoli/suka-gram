package com.example.sukagram.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.sukagram.DTO.UserDTO;
import com.example.sukagram.Exception.*;
import com.example.sukagram.model.*;
import com.example.sukagram.service.AvatarService;
import com.example.sukagram.service.FriendShipService;
import com.example.sukagram.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final FriendShipService friendShipService;
    private final AvatarService avatarService;

    @Autowired
    public UserController(UserService userService, FriendShipService friendShipService, AvatarService avatarService) {
        this.userService = userService;
        this.friendShipService = friendShipService;
        this.avatarService = avatarService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/registration")
    public ResponseEntity<User> saveUser(@RequestBody UserDTO userDTO) throws Status434UserNicknameNotUniqueException {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/save").toUriString());
        return ResponseEntity.ok().body(userService.saveUser(userDTO));
    }

    @PostMapping("/set-avatar")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                               @RequestHeader("Authorization") String token) throws IOException, Status443FileIsNullException {
        return ResponseEntity.ok().body(avatarService.setAvatar(file,token));
    }

    @GetMapping("/get-user/{userName}")
    public ResponseEntity<User> getUserByName(@PathVariable String userName){
        return ResponseEntity.ok().body(userService.getUser(userName).get());
    }

    @GetMapping("/get-userById/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId){
        return ResponseEntity.ok().body(userService.getById(userId).get());
    }

    @PostMapping("/user/follow-up/{followingId}")
    public ResponseEntity<FriendShip> followUp(@PathVariable String followingId, @RequestHeader("Authorization") String  token) throws Status430UserNotFoundException, Status433FriendShipAlreadyExistsException, Status432SelfFollowingException {
        friendShipService.followUp(followingId, token);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/user/unfollow-up/{followingId}")
    public ResponseEntity<FriendShip> unFollow(@PathVariable String followingId, @RequestHeader("Authorization") String  token) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException {
        friendShipService.unFollow(followingId, token);
        return new ResponseEntity<>(OK);
    }

    @GetMapping("/get-followers-user/{userId}")
    public ResponseEntity<List<User>> getFollowersByUserId(@PathVariable String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException {
        return ResponseEntity.ok().body(friendShipService.getFollowersByUserId(userId));
    }

    @GetMapping("/get-following-user/{userId}")
    public ResponseEntity<List<User>> getFollowingByUserId(@PathVariable String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException {
        return ResponseEntity.ok().body(friendShipService.getFollowingByUserId(userId));
    }


//    @PostMapping("/role/save")
//    public ResponseEntity<Role> saveRole(@RequestBody Role role){
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/role/save").toUriString());
//        return ResponseEntity.created(uri).body(userService.saveRole(role));
//    }

//    @PostMapping("/role/addToUser")
//    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
//        userService.addRoleToUser(form.getRoleName(), form.getUserName());
//        return ResponseEntity.ok().build();
//    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String name = decodedJWT.getSubject();
                Optional<User> user = userService.getUser(name);
                String access_token = JWT.create()
                        .withSubject(user.get().getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
//                        .withClaim("roles",user.get().getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                //  response.sendError(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);

                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }else {
         throw new RuntimeException("Refresh token is missing");
        }
    }
}

