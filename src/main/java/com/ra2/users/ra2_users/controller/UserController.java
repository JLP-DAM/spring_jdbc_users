package com.ra2.users.ra2_users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

// Controlador
@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // Endpoint per crear usuaris
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

    // Endpoint per llegir tots els usuaris
    @GetMapping("/users")
    public ResponseEntity<String> getAllUsers() {
        List<User> users = userRepository.getAllUsers();

        return ResponseEntity.status(users.size() <= 0 ? HttpStatus.NOT_FOUND : HttpStatus.FOUND).body(users.size() <= 0 ? "No s'ha trobat cap usuari" : "Usuaris trobats: " + users);
    }

    // Endpoint per llegir un usuari segons la seva id
    @GetMapping("/users/{user_id}")
    public ResponseEntity<String> getUser(@PathVariable long user_id) {
        User user = userRepository.getUser(user_id);

        return ResponseEntity.status(user == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND).body(user == null ? "No s'ha trobat cap usuari" : "Usuari trobat: " + user);
    }

    // Endpoint per actualitzar un usuari de forma completa segons la seva id
    @PutMapping("/users/{user_id}")
    public ResponseEntity<String> updateUser(@PathVariable long user_id, @RequestBody User user) {
         try {
            userRepository.updateUser(user_id, user);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut actualitzar l'Usuari: \"" + user + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha actualitzat l'usuari: " + user);
    }

     // Endpoint per actualitzar el nom d'un usuari segons la seva id
    @PatchMapping("/users/{user_id}/name")
    public ResponseEntity<String> updateUserName(@PathVariable long user_id, @RequestParam String name) {
         try {
            userRepository.updateUserName(user_id, name);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut actualitzar l'Usuari amb id: \"" + user_id + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha actualitzat el nom de l'usuari amb id: " + user_id);
    }

    // Endpoint per actualitzar el nom d'un usuari segons la seva id
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<String> deleteUser(@PathVariable long user_id) {
         try {
            userRepository.deleteUser(user_id);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut esborrar l'Usuari amb id: \"" + user_id + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha esborrat el nom de l'usuari amb id: " + user_id);
    }
}










// odio haver d'escriure de totes aquestes maneres per seguir els standards de codi "PascalCase per classes, camelCase per variables, LOUD_CASE per constants, etc"
// porto 5 anys escrivint-ho tot amb PascalCase perque ho trobo mes facil de llegir i no he tingut cap problema i ho trobo a faltar a classe :(
// https://tenor.com/view/black-man-crying-cry-crying-crying-emoji-crying-meme-gif-3223392509479903783