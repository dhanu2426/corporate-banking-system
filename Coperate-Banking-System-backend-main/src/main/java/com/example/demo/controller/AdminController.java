package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // Remove passwords before sending
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }
    
    @PutMapping("/users/{id}/status")
    public ResponseEntity<User> updateUserStatus(
            @PathVariable String id,
            @RequestBody Map<String, Boolean> request) {
        
        Boolean active = request.get("active");
        User user = userService.updateUserStatus(id, active);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}