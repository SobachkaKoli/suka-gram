package com.example.sukagram.serviceImpl;

import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.Exception.Status432SelfFollowingException;
import com.example.sukagram.Exception.Status433FriendShipAlreadyExistsException;
import com.example.sukagram.Exception.Status442FriendShipDoesntExistsException;
import com.example.sukagram.model.FriendShip;
import com.example.sukagram.model.User;
import com.example.sukagram.repository.FriendShipRepository;
import com.example.sukagram.service.FriendShipService;
import com.example.sukagram.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendShipServiceImpl implements FriendShipService{
    private final FriendShipRepository friendShipRepository;
    private final UserService userService;


    public FriendShipServiceImpl(FriendShipRepository friendShipRepository, UserService userService) {
        this.friendShipRepository = friendShipRepository;
        this.userService = userService;

    }

    @Override
    public void followUp(String followingId, String token) throws Status430UserNotFoundException, Status432SelfFollowingException, Status433FriendShipAlreadyExistsException {

        if (friendShipRepository.existsByFollowerAndFollowing(
                userService.getAuthenticatedUser(token).get(),userService.getById(followingId).get())){
            throw new Status433FriendShipAlreadyExistsException(followingId);
        }else {
            if(!userService.existsById(followingId)){
                throw new Status430UserNotFoundException(followingId);
            }else if(userService.getAuthenticatedUser(token).get().getId().equals(followingId)){
                throw new Status432SelfFollowingException("You can not follow up yourself");
            }else {
                FriendShip friendShip = FriendShip
                        .builder()
                        .following(userService.getById(followingId).get())
                        .follower(userService.getAuthenticatedUser(token).get())
                        .build();
                friendShipRepository.save(friendShip);
            }
        }
    }

    @Override
    public void unFollow(String friendId, String token) throws Status442FriendShipDoesntExistsException {

        if(!friendShipRepository.existsByFollowerAndFollowing(userService.getAuthenticatedUser(token).get(),
                userService.getById(friendId).get()
                )){
            throw new Status442FriendShipDoesntExistsException(userService.getById(friendId).get().getUserName());
        }else {
            friendShipRepository.deleteFriendShipByFollowerAndFollowing(userService.getAuthenticatedUser(token).get(),
                    userService.getById(friendId).get());
        }
    }    @Override
    public List<User> getFollowersByUserId(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException {

        if(!userService.existsById(userId)){
            throw new Status430UserNotFoundException(userId);
        }else {
            Optional<User> user = userService.getById(userId);
            if (!friendShipRepository.existsByFollowing(user.get())) {
                throw new Status442FriendShipDoesntExistsException(user.get().getUserName());
            }else {

                List<User> followers = new ArrayList<>();
                for (FriendShip friendShip : friendShipRepository.findAllByFollowing(user.get())) {
                    followers.add(friendShip.getFollower());
                }
                return followers;
            }
        }
    }




//    @Override
//    public List<User> getFollowersByUserId(String followingId) throws Status442FriendShipDoesntExistsException {
//
//            if (!friendShipRepository.existsByFollowingId(followingId)){
//                throw new Status442FriendShipDoesntExistsException(userService.getById(followingId).get().getName());
//            }else {
//                Optional<User> user = userService.getById(followingId);
//                List<User> followers = new ArrayList<>();
//                for (FriendShip friendShip : friendShipRepository.findAllByFollowing(user.get())) {
//                    followers.add(friendShip.getFollower());
//                }
//                return followers;
//            }
//        }

    @Override
    public List<User> getFollowingByUserId(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException {
        if (!userService.existsById(userId)){
            throw new Status430UserNotFoundException(userId);
        }else {
            Optional<User> user = userService.getById(userId);
            if(!friendShipRepository.existsByFollower(user.get())){
                throw new Status442FriendShipDoesntExistsException(user.get().getUserName());
            }
            List<User> following = new ArrayList<>();
            for(FriendShip friendShip : friendShipRepository.findAllByFollower(user.get())){
                following.add(friendShip.getFollowing());
            }
            return following;
        }

    }
}
