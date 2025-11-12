package com.ra2.users.ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.service.UserService;

// Controlador
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint per crear usuaris
    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // Endpoint per llegir tots els usuaris
    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        return userService.getAllUsers();
    }

    // Endpoint per llegir un usuari segons la seva id
    @GetMapping("/users/{user_id}")
    public ResponseEntity<String> getUser(@PathVariable long user_id) {
        return userService.getUser(user_id);
    }

    // Endpoint per actualitzar un usuari de forma completa segons la seva id
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable long user_id, @RequestBody User user) {
        return userService.updateUser(user_id, user);
    }

     // Endpoint per actualitzar el nom d'un usuari segons la seva id
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> updateUserName(@PathVariable long user_id, @RequestParam String name) {
        return userService.updateUserName(user_id, name);
    }

    // Endpoint per actualitzar el nom d'un usuari segons la seva id
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable long user_id) {
        return userService.deleteUser(user_id);
    }

    // Endpoint per pujar una foto per un usuari
    @PostMapping("/users/{user_id}/image")
    public ResponseEntity<String> uploadImage(@PathVariable long user_id, @RequestParam MultipartFile imageFile) throws Exception {
        return userService.uploadImage(user_id, imageFile);
    }

    // Endpoint per pujar usuaris en massa a través d'un csv
    @PostMapping("/users/upload-csv")
    public ResponseEntity<String> bulkLoadUsers(@RequestParam MultipartFile csvFile) throws Exception {
        return userService.bulkLoadUsers(csvFile);
    }
}