package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SaleDAO {
    public static int insertSale(int userId, double totalAmount){
        String insertSaleSQL = "INSERT INTO Sale (user_id, total_amount,  sale_date) VALUES (?,?, CURRENT_TIMESTAMP)";

        try (Connection conn = Inventory.connect(); PreparedStatement pstmt = conn.prepareStatement(insertSaleSQL)) {
            pstmt.setInt(1, userId);  
            pstmt.setDouble(2, totalAmount);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Return the generated Sale ID
                }
            }
            }
            System.out.println("Sale inserted successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting sale: " + e.getMessage());
        }
        return 0;
    }
}
