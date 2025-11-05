package com.ra2.users.ra2_users.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

// El servei, l'intermediari entre el repositori i el controlador
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Creador d'usuaris
    public ResponseEntity<String> createUser(User user) {
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

    // Obten tots els usuaris
    public ResponseEntity<String> getAllUsers() {
        List<User> users = userRepository.getAllUsers();

        return ResponseEntity.status(users.size() <= 0 ? HttpStatus.NOT_FOUND : HttpStatus.FOUND).body(users.size() <= 0 ? "No s'ha trobat cap usuari" : "Usuaris trobats: " + users);
    }

    // Obten un usuari a partir de la seva user id
    public ResponseEntity<String> getUser(long user_id) {
        User user = userRepository.getUser(user_id);

        return ResponseEntity.status(user == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND).body(user == null ? "No s'ha trobat cap usuari" : "Usuari trobat: " + user);
    }

    // Actualitzem (mes be sobreescribim) un usuari de forma completa a través de la seva user id
    public ResponseEntity<String> updateUser(long user_id, User user) {
        try {
            userRepository.updateUser(user_id, user);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut actualitzar l'Usuari: \"" + user + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha actualitzat l'usuari: " + user);
    }

    // Actualitzem el nom d'un usuari de forma completa a través de la seva user id
    public ResponseEntity<String> updateUserName(long user_id, String name) {
        try {
            userRepository.updateUserName(user_id, name);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut actualitzar l'Usuari amb id: \"" + user_id + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha actualitzat el nom de l'usuari amb id: " + user_id);
    }

    // Borrem un usuari a través de la seva user id
    public ResponseEntity<String> deleteUser(long user_id) {
        try {
            userRepository.deleteUser(user_id);
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut esborrar l'Usuari amb id: \"" + user_id + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha esborrat el nom de l'usuari amb id: " + user_id);
    }

    // Borrem un usuari a través de la seva user id
    public ResponseEntity<String> uploadImage(long user_id, MultipartFile imageFile) {
        User user = userRepository.getUser(user_id);

        System.out.println(imageFile);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No s'ha pogut trobar l'usuari amb la id: \"" + user_id + "\".");
        }

        user.setImageFile(imageFile);

        if (imageFile != null) {
            
        }

        updateUser(user_id, user);

        return ResponseEntity.status(HttpStatus.OK).body("S'ha pujat la foto de l'usuari amb id: " + user_id);
    }
}
