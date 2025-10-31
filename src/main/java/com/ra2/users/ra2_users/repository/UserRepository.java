package com.ra2.users.ra2_users.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
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

            return user;
        }
    }

    public void insertUser(User user) {
        jdbcTemplate.update("insert into users (name, description, email, password, ultimAcces, dataCreated, dataUpdated) values (?, ?, ?, ?, ?, ?, ?)", user.getName(), user.getDescription(), user.getEmail(), user.getPassword(), user.getUltimAcces(), user.getDataCreated(), user.getDataUpdated());
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("select id, name, description, email, password, ultimAcces, dataCreated, dataUpdated from users", new UserRowMapper());
    }
}