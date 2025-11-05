package com.ra2.users.ra2_users.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.ra2.users.ra2_users.model.User;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Objecte que utilitzem per representar el resultSet que ens retorna un query per poder interpretar les columnes
    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setDescription(resultSet.getString("description"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setUltimAcces(resultSet.getTimestamp("ultimAcces"));
            user.setDataCreated(resultSet.getTimestamp("dataCreated"));
            user.setDataUpdated(resultSet.getTimestamp("dataUpdated"));
            //user.setImageFile(resultSet.getString("imageFile"));            

            return user;
        }
    }

    // Insertem un usuari a partir d'una instancia de l'objecte
    public void insertUser(User user) {        
        jdbcTemplate.update("insert into users (name, description, email, password, ultimAcces, dataCreated, dataUpdated) values (?, ?, ?, ?, ?, ?, ?)", user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), user.getUltimAcces(), user.getDataCreated(), user.getDataUpdated());
    }

    // Llegim tots els usuaris a la taula
    public List<User> getAllUsers() {
        jdbcTemplate.update("update users set ultimAcces = ?", (new Timestamp(System.currentTimeMillis())));
        
        return jdbcTemplate.query("select * from users", new UserRowMapper());
    }

    // Trobem a un usuari per la seva Id
    public User getUser(long userId) {
        jdbcTemplate.update(String.format("update users set ultimAcces = ? where id = %s", userId), (new Timestamp(System.currentTimeMillis())));
        List<User> users = jdbcTemplate.query(String.format("select * from users where id = %s", userId), new UserRowMapper());

        return users.size() <= 0 ? null : users.get(0);
    }

    // Actualitzem a un usuari de forma completa per la seva Id
    public void updateUser(long userId, User user) {
        user.setId(userId);
        user.setDataUpdated(new Timestamp(System.currentTimeMillis()));
        jdbcTemplate.update(String.format("update users set name = ?, description = ?, email = ?, password = ?, ultimAcces = ?, dataCreated = ?, dataUpdated = ? where id = %s", user.getId()), user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), user.getUltimAcces(), user.getDataCreated(), user.getDataUpdated());
    }

    // Actualitzem el nom d'un usuari per la seva Id
    public void updateUserName(long userId, String name) {
        jdbcTemplate.update(String.format("update users set dataUpdated = ? where id = %s", userId), (new Timestamp(System.currentTimeMillis())));
        jdbcTemplate.update(String.format("update users set name = ?, dataUpdated = ? where id = %s", userId), name, (new Timestamp(System.currentTimeMillis())));
    }

    // Borrem un usuari per la seva Id
    public void deleteUser(long userId) {
        jdbcTemplate.execute(String.format("delete from users where id = %s", userId));
    }
}