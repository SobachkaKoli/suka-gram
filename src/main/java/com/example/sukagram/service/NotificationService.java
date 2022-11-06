package com.example.sukagram.service;

import com.example.sukagram.model.User;

public interface NotificationService {

    void sendNotification(String token, String recipient, String message);

}
