package com.example.sukagram.controller;

import com.example.sukagram.Exception.Status430UserNotFoundException;
import com.example.sukagram.model.User;
import com.example.sukagram.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {


    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }

    @PatchMapping("/ban-user/{userId}")
    public ResponseEntity<User> banUser(@PathVariable String userId ) throws Status430UserNotFoundException {
        return ResponseEntity.ok().body(adminService.banUserById(userId));
    }

    @PatchMapping("/unban-user/{userId}")
    public ResponseEntity<User> unBanUser(@PathVariable String userId ) throws Status430UserNotFoundException {
        return ResponseEntity.ok().body(adminService.unBanUserById(userId));
    }

}
