package com.school.dao;

import com.school.model.User;
import com.school.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User authenticate(String username, String password, String role) {
        String sql = "SELECT u.id, u.username, u.password, u.full_name, u.email, r.role_name " +
                     "FROM users u JOIN roles r ON u.role_id = r.id " +
                     "WHERE u.username = ? AND u.password = ? AND r.role_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role_name"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(User user) {
        String getRoleIdSql = "SELECT id FROM roles WHERE role_name = ?";
        String insertUserSql = "INSERT INTO users (username, password, full_name, email, role_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement getRoleStmt = conn.prepareStatement(getRoleIdSql);
             PreparedStatement insertUserStmt = conn.prepareStatement(insertUserSql)) {
            // Get role id
            getRoleStmt.setString(1, user.getRole());
            ResultSet rs = getRoleStmt.executeQuery();
            if (rs.next()) {
                int roleId = rs.getInt("id");
                // Insert user
                insertUserStmt.setString(1, user.getUsername());
                insertUserStmt.setString(2, user.getPassword());
                insertUserStmt.setString(3, user.getFullName());
                insertUserStmt.setString(4, user.getEmail());
                insertUserStmt.setInt(5, roleId);
                int rows = insertUserStmt.executeUpdate();
                return rows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
