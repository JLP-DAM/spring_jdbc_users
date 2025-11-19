package com.ra2.users.ra2_users.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.ra2.users.ra2_users.model.User;
import com.ra2.users.ra2_users.repository.UserRepository;

// El servei, l'intermediari entre el repositori i el controlador
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper objectMapper;

    // Creador d'usuaris
    public ResponseEntity<String> createUser(User user) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());

        user.setDataCreated(currentDate);
        user.setDataUpdated(currentDate);

        try {
            userRepository.insertUser(user);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No s'ha pogut crear l'Usuari: \"" + user + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("Afegit l'usuari " + user);
    }

    // Obten tots els usuaris
    public ResponseEntity<String> getAllUsers() {
        List<User> users = userRepository.getAllUsers();

        return ResponseEntity.status(users.size() <= 0 ? HttpStatus.NOT_FOUND : HttpStatus.FOUND)
                .body(users.size() <= 0 ? "No s'ha trobat cap usuari" : "Usuaris trobats: " + users);
    }

    // Obten un usuari a partir de la seva user id
    public ResponseEntity<String> getUser(long user_id) {
        User user = userRepository.getUser(user_id);

        return ResponseEntity.status(user == null ? HttpStatus.NOT_FOUND : HttpStatus.FOUND)
                .body(user == null ? "No s'ha trobat cap usuari" : "Usuari trobat: " + user);
    }

    // Actualitzem (mes be sobreescribim) un usuari de forma completa a través de la
    // seva user id
    public ResponseEntity<String> updateUser(long user_id, User user) {
        try {
            userRepository.updateUser(user_id, user);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    "No s'ha pogut actualitzar l'Usuari: \"" + user + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha actualitzat l'usuari: " + user);
    }

    // Actualitzem el nom d'un usuari de forma completa a través de la seva user id
    public ResponseEntity<String> updateUserName(long user_id, String name) {
        try {
            userRepository.updateUserName(user_id, name);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut actualitzar l'Usuari amb id: \""
                    + user_id + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha actualitzat el nom de l'usuari amb id: " + user_id);
    }

    // Borrem un usuari a través de la seva user id
    public ResponseEntity<String> deleteUser(long user_id) {
        try {
            userRepository.deleteUser(user_id);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha pogut esborrar l'Usuari amb id: \""
                    + user_id + "\", error: " + exception.getLocalizedMessage());
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'ha esborrat el nom de l'usuari amb id: " + user_id);
    }

    // Actualitzem un usuari per donar-li una imatge
    public ResponseEntity<String> uploadImage(long user_id, MultipartFile imageFile) throws Exception {
        User user = userRepository.getUser(user_id);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No s'ha pogut trobar l'usuari amb la id: \"" + user_id + "\".");
        }

        String imagePath = null;

        if (imageFile != null) {
            File imageFolder = new File("src/main/resources/private/images");

            if (!imageFolder.exists()) {
                imageFolder.mkdirs();
            }

            imagePath = "src/main/resources/private/images/" + user.getId() + ".png";

            File image = new File("src/main/resources/private/images/" + user.getId() + ".png");

            try (OutputStream outputStream = new FileOutputStream(image)) {
                outputStream.write(imageFile.getBytes());
            }
        }

        user.setImagePath(imagePath);

        updateUser(user_id, user);

        return ResponseEntity.status(HttpStatus.OK)
                .body("S'ha pujat la foto de l'usuari amb id: " + user_id + " al path: " + imagePath);
    }

    // Carregem multiples usuaris a través d'un csv
    public ResponseEntity<String> bulkLoadUsersCSV(@RequestParam MultipartFile csvFile) throws Exception {

        if (csvFile == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha enviat cap fitxer CSV");
        }

        if (csvFile.getOriginalFilename().indexOf(".csv") == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El fitxer enviat no es CSV");
        }

        File csvFolder = new File("src/main/resources/csv_processed");

        if (!csvFolder.exists()) {
            csvFolder.mkdirs();
        }

        File regularCSVFile = new File("src/main/resources/csv_processed/" + csvFile.getOriginalFilename());

        ArrayList<User> users = new ArrayList<User>();

        try (OutputStream outputStream = new FileOutputStream(regularCSVFile)) {
            outputStream.write(csvFile.getBytes());
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(regularCSVFile))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                ArrayList<String> values = new ArrayList<String>(Arrays.asList(line.split(",")));

                User user = new User();

                user.setName(values.size() >= 1 ? values.get(0) : null);
                user.setDescription(values.size() >= 2 ? values.get(1) : null);
                user.setEmail(values.size() >= 3 ? values.get(2) : null);
                user.setPassword(values.size() >= 4 ? values.get(3) : null);

                users.add(user);
            }
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error llegint el CSV " + exception.getLocalizedMessage());
        }

        for (User user: users) {
            ResponseEntity<String> responseEntity = createUser(user);

            if (responseEntity.getStatusCode() != HttpStatus.CONFLICT) {continue;}

            return responseEntity;
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'han introduit tots els usuaris del CSV.");
    }

    // Carregem multiples usuaris a través d'un csv
    public ResponseEntity<String> bulkLoadUsersJSON(@RequestParam MultipartFile jsonFile) throws Exception {
        if (jsonFile == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("No s'ha enviat cap fitxer JSON");
        }

        if (jsonFile.getOriginalFilename().indexOf(".json") == -1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El fitxer enviat no es JSON");
        }

        File jsonFolder = new File("src/main/resources/json_processed");

        if (!jsonFolder.exists()) {
            jsonFolder.mkdirs();
        }

        File regularjsonFile = new File("src/main/resources/json_processed/" + jsonFile.getOriginalFilename());

        try (OutputStream outputStream = new FileOutputStream(regularjsonFile)) {
            outputStream.write(jsonFile.getBytes());
        }

        JsonNode users = objectMapper.readTree(regularjsonFile).path("data").path("users");

        for (JsonNode jsonUser: users) {
            User user = objectMapper.treeToValue(jsonUser, User.class);

            ResponseEntity<String> responseEntity = createUser(user);

            if (responseEntity.getStatusCode() != HttpStatus.CONFLICT) {continue;}

            return responseEntity;
        }

        return ResponseEntity.status(HttpStatus.OK).body("S'han introduit tots els usuaris del json.");
    }
}