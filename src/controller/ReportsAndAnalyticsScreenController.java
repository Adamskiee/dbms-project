package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.Inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.SoldItemTransaction;

public class ReportsAndAnalyticsScreenController {
    
    @FXML
    private TableColumn<SoldItemTransaction, String> columnName;

    @FXML
    private TableColumn<SoldItemTransaction, Integer> columnQuantity;

    @FXML
    private TableColumn<SoldItemTransaction, Integer> columnSaleID;

    @FXML
    private TableColumn<SoldItemTransaction, String> columnSaleDate;

    @FXML
    private TableColumn<SoldItemTransaction, Double> columnSubtotal;

    @FXML
    private TableView<SoldItemTransaction> tableView;
    
    private ObservableList<SoldItemTransaction> transactionList;
    @FXML
    void initialize(){
        columnName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        columnSaleID.setCellValueFactory(new PropertyValueFactory<>("saleId"));
        columnSaleDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        loadTransactionData();
    }

    void loadTransactionData(){
        transactionList = FXCollections.observableArrayList();

        String query = """
                SELECT 
                    Sale.sale_id, 
                    Sale.sale_date, 
                    Sale.total_amount, 
                    Product.product_name, 
                    Sale_Item.quantity, 
                    Sale_Item.subtotal 
                FROM 
                    Sale 
                INNER JOIN 
                    Sale_Item 
                ON 
                    Sale.sale_id = Sale_Item.sale_id 
                INNER JOIN 
                    Product 
                ON 
                    Sale_Item.product_id = Product.product_id;
            """;

        try (Connection conn = Inventory.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                SoldItemTransaction soldItemTransaction = new SoldItemTransaction(
                    rs.getInt("sale_id"), 
                    rs.getString("sale_date"), 
                    rs.getString("product_name"), 
                    rs.getInt("quantity"),
                    rs.getDouble("subtotal")
                    );
                transactionList.add(soldItemTransaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set data to TableView
        tableView.setItems(transactionList);
    }
}
