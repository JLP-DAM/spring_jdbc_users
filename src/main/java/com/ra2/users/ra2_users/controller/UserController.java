package com.ra2.users.ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        user.setDataCreated(currentDate);
        user.setDataUpdated(currentDate);

        try {
            userRepository.insertUser(user);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut crear l'Usuari: \"" + user + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Afegit l'usuari " + user);
    }

    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        List<User> users = userRepository.getAllUsers();

        return ResponseEntity.status(HttpStatus.OK).body(users.size() <= 0 ? "No s'ha trobat cap usuari" : "Usuaris trobats" + users);
    }
}