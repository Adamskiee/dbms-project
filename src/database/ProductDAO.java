package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.Product;

public class ProductDAO {
    public static void insertProduct(String productName, double price, int stockQuantity){
        String insertProductSQL = "INSERT INTO Product (product_name, price, stock_quantity) VALUES (?, ?, ?)";

        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(insertProductSQL)) {
            pstmt.setString(1, productName);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, stockQuantity);
            pstmt.executeUpdate();
            System.out.println("Product inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting product: " + e.getMessage());
        }
    }

    public static void updateProductName(int productId, String name) { 
        String query = "UPDATE Product SET product_name = ? WHERE product_id = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, productId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public static void updateProductPrice(int productId, double price) { 
        String query = "UPDATE Product SET price = ? WHERE product_id = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, price);
            pstmt.setInt(2, productId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public static void updateStockQuantity(int productId, int stock_quantity) { 
        String query = "UPDATE Product SET stock_quantity = ? WHERE product_id = ?";
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, stock_quantity);
            pstmt.setInt(2, productId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Product updated successfully.");
            } else {
                System.out.println("Product not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }
    
    public static void deleteProduct(int productId) {
        String query = "DELETE FROM Product WHERE product_id = ?";
    
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    public static boolean isProductExist(int productId) {
        String query = "SELECT * FROM Product WHERE product_id = ?";
    
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return false;
    }

    public static Product getProductById(int productId) {
        String query = "SELECT * FROM Product WHERE product_id = ?";
        Product product = null;
    
        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
    
            if (rs.next()) {
                product = new Product(
                    rs.getInt("product_id"),
                    rs.getString("product_name"),
                    rs.getDouble("price"),
                    rs.getInt("stock_quantity")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error fetching product: " + e.getMessage());
        }
        return product;
    }
}
