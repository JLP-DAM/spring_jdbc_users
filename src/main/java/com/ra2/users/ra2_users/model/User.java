package com.ra2.users.ra2_users.model;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

// Classe per representar les dades d'un usuari
// Té setters i getters per cada propietat
public class User {
    long id;
    String name;
    String description;
    String email;
    String password;
    Timestamp ultimAcces;
    Timestamp dataCreated;
    Timestamp dataUpdated;
    MultipartFile imageFile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Timestamp getUltimAcces() {
        return ultimAcces;
    }

    public void setUltimAcces(Timestamp ultimAcces) {
        this.ultimAcces = ultimAcces;
    }


    public Timestamp getDataCreated() {
        return dataCreated;
    }
    
    public void setDataCreated(Timestamp dataCreated) {
        this.dataCreated = dataCreated;
    }


    public Timestamp getDataUpdated() {
        return dataUpdated;
    }
    
    public void setDataUpdated(Timestamp dataUpdated) {
        this.dataUpdated = dataUpdated;
    }
    
    
    public MultipartFile getImageFile() {
        return imageFile;
    }
    
    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", description=" + description + ", email=" + email + ", password="
                + password + ", ultimAcces=" + ultimAcces + ", dataCreated=" + dataCreated + ", dataUpdated="
                + dataUpdated + "]";
    }
}