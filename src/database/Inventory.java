package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Inventory {
    private static final String URL = "jdbc:sqlite:database/inventory.db"; // SQLite file

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    
    
    
    

    

    
    

    

    

}