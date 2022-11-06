package com.example.sukagram.service;

import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.Exception.Status432SelfFollowingException;
import com.example.sukagram.Exception.Status433FriendShipAlreadyExistsException;
import com.example.sukagram.Exception.Status442FriendShipDoesntExistsException;
import com.example.sukagram.model.User;

import java.util.List;

public interface FriendShipService {
   void followUp(String newFriend, String token) throws Status430UserNotFoundException, Status432SelfFollowingException, Status433FriendShipAlreadyExistsException;

   void unFollow(String friendId , String token) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException;

   List<User> getFollowersByUserId(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException;
   List<User> getFollowingByUserId(String userId) throws Status430UserNotFoundException, Status442FriendShipDoesntExistsException;
}
