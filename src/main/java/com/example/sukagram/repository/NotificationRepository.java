package com.example.sukagram.repository;

import com.example.sukagram.notifications.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,String > {
}
