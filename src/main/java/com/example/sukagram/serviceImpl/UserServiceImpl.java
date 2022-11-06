package com.example.sukagram.serviceImpl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.sukagram.DTO.UserDTO;
import com.example.sukagram.Exception.Status434UserNicknameNotUniqueException;
import com.example.sukagram.model.User;
import com.example.sukagram.model.Role;
import com.example.sukagram.repository.UserRepository;
import com.example.sukagram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;

@Service
@Transactional
@Slf4j
public class UserServiceImpl implements UserService{

    @Value("${upload.path}")
    private String uploadPath;
    private final UserRepository userRepository;


//    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,  PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;
    }
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Optional<User> user = userRepository.findByUserName(userName);
//        if(user.isEmpty()){
//            log.error("User not found in the database");
//            throw  new UsernameNotFoundException("User not found in the database");
//        }else {
//            log.info("User found in the database: {}",userName);
//        }
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//
//            authorities.add(new SimpleGrantedAuthority(user.get().getRole().name()));
//
//        return new org.springframework.security.core.userdetails.User(
//                user.get().getUserName(),user.get().getPassword(),authorities);
//    }
    @Override
    public User saveUser(UserDTO userDTO) throws Status434UserNicknameNotUniqueException {
        if (userRepository.existsByUserName(userDTO.getUserName())) {
            throw new Status434UserNicknameNotUniqueException();
        }
        List<Role> roles = new ArrayList<>();
        roles.add(Role.USER);
        User user = User.builder()
                .email(userDTO.getEmail())
                .userName(userDTO.getUserName())
                .password(userDTO.getPassword())
                .role(Role.USER)
                .build();
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        File userDir = new File(uploadPath + "/" + user.getUserName());
        userDir.mkdir();

        File userAvatarsDir = new File(uploadPath + "/" + user.getUserName() + "/avatars");
        userAvatarsDir.mkdir();

        File userPostsDir = new File(uploadPath + "/" + user.getUserName() + "/posts");
        userPostsDir.mkdir();


        log.info("Saving new user {} to the database", user.getUserName());
        System.out.println(user);

        return userRepository.save(user);
    }

//    @Override
//    public Role saveRole(Role role) {
//        log.info("Saving new role {} to the database", role.name());
//        return roleRepository.save(role);
//    }
//
//    @Override
//    public void addRoleToUser(String roleName, String userName) {
//        log.info("Adding role {} to user {}", roleName, userName);
//        Optional<User> user = userRepository.findByUserName(userName);
//        Role role = roleRepository.findByName(roleName);
//        userRepository.save(user.get());
//
//    }

    @Override
    public Optional<User> getUser(String userName) {
        log.info("Fetching user {}", userName);
        return userRepository.findByUserName(userName);
    }

    @Override
    public Optional<User> getById(String userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getAuthenticatedUser(String token) {
        return userRepository.findByUserName(JWT.decode(validateToken(token)).getSubject());
    }
    public String validateToken(String token){
        return token.substring(7);
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetching users");
        return userRepository.findAll();
    }

    @Override
    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

//    @Override
//    public Optional<User> findAuthenticatedUser() {
//
//        return userRepository
//                .findByName(SecurityContextHolder
//                .getContext()
//                .getAuthentication()
//                .getName());
//    }

    @Override
    public DecodedJWT decodedJWT(String token) {
        Base64.getDecoder().decode(JWT.decode(token).getSubject());
        JWT.decode(token);
        return JWT.decode(token);
    }
}
