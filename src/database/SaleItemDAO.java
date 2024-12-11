package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaleItemDAO {
    public static void insertSaleItem(int saleId, int productId, int quantity, double subtotal){
        String insertSaleItemSQL = "INSERT INTO Sale_Item (sale_id, product_id, quantity, subtotal) VALUES (?, ?, ?, ?)";

        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(insertSaleItemSQL)) {
            pstmt.setInt(1, saleId);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, quantity);
            pstmt.setDouble(4, subtotal);
            pstmt.executeUpdate();
            int updatedStock = ProductDAO.getProductById(productId).getStockQuantity() - quantity;
            ProductDAO.updateStockQuantity(productId, updatedStock);
            System.out.println("Sale Item inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting sale item: " + e.getMessage());
        }   
    }
}
