package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.User;

public class UserDAO {
    
    public static void insertUser(String username, String password, String role){
        String insertUserSQL = "INSERT INTO User (username, password, role) VALUES (?, ?, ?)";

        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);  // In a real-world app, make sure to hash passwords
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            System.out.println("User inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting user: " + e.getMessage());
        }
    }

    public static boolean authenticateUsername(String username){
        String query = "SELECT * FROM User WHERE username = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
        }
        return true;
    }

    public static User authenticateUser(String username, String password) {
        String query = "SELECT user_id, role FROM User WHERE username = ? AND password = ?";
        
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);  // In a real-world app, hash the password
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String user_role = rs.getString("role");
                int user_id = rs.getInt("user_id");
                return new User(user_id, username, password, user_role);
            } else {
                System.out.println("Invalid username or password!");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error authenticating user: " + e.getMessage());
            return null;
        }
    }

    public static User getUser(int userId){
        String query = "SELECT * FROM User WHERE user_id = ?";
        User user = null;
    
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                user = new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("role")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return user;
    }

    public static boolean isUserExist(int userId){
        String query = "SELECT * FROM User WHERE user_id = ?";
    
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return false;
    }

    public static void updateUsername(String username, int user_id){
        String query = "UPDATE User SET username = ? WHERE user_id = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setInt(2, user_id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
    public static void updatePassword(String password, int user_id){
        String query = "UPDATE User SET password = ? WHERE user_id = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, password);
            pstmt.setInt(2, user_id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }
    public static void updateRole(String role, int user_id){
        String query = "UPDATE User SET role = ? WHERE user_id = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, role);
            pstmt.setInt(2, user_id);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User updated successfully.");
            } else {
                System.out.println("User not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating user: " + e.getMessage());
        }
    }

    public static boolean removeUser(int userId){
        String query = "DELETE FROM User WHERE user_id = ?";
    
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
    
            if (rowsAffected > 0) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
        return false;
    }
}