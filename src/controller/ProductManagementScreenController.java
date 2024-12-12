package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import database.Inventory;
import database.ProductDAO;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import models.Product;
import models.User;

public class ProductManagementScreenController {
    
    public static User user;
    
    @FXML
    private TableColumn<Product, Integer> idColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, Double> priceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableView<Product> tableView;

    @FXML
    private TextField txtfieldName_addSec;

    @FXML
    private TextField txtfieldPrice_addSec;

    @FXML
    private TextField txtfieldQuantity_addSec;

    @FXML
    private TextField txtfieldId_removeSec;

    @FXML
    private TextField txtfieldId_updateSec;

    @FXML
    private TextField txtfieldName_updateSec;

    @FXML
    private TextField txtfieldPrice_updateSec;

    @FXML
    private TextField txtfieldQuantity_updateSec;

    @FXML
    private Label lblMessage_addsection;
    
    @FXML
    private Label lblMessage_updatesection;
    
    @FXML
    private Label lblMessage_removesection;

    @FXML
    private CheckBox chckboxName;

    @FXML
    private CheckBox chckboxPrice;

    @FXML
    private CheckBox chckboxQuantity;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonSearch;

    @FXML
    private Button buttonUpdate;

    @FXML
    private Button buttonRemove;

    @FXML
    private BorderPane borderPane;

    private ObservableList<Product> productList;

    @FXML
    void chckboxName_onClicked(ActionEvent event) {
        if(chckboxName.isSelected()){
            txtfieldName_updateSec.setDisable(false); //asdfasdf
        }else{
            txtfieldName_updateSec.setDisable(true);
        }
    }

    @FXML
    void chckboxPrice_onClicked(ActionEvent event) {
        if(chckboxPrice.isSelected()){
            txtfieldPrice_updateSec.setDisable(false);
        }else{
            txtfieldPrice_updateSec.setDisable(true);
        }
    }

    @FXML
    void chckboxQuantity_onClicked(ActionEvent event) {
        if(chckboxQuantity.isSelected()){
            txtfieldQuantity_updateSec.setDisable(false);
        }else{
            txtfieldQuantity_updateSec.setDisable(true);
        }
    }

    @FXML
    void txtfieldName_addSec_onCLicked(MouseEvent event) {
        lblMessage_addsection.setVisible(false);
        txtfieldPrice_addSec.setText("");
        txtfieldQuantity_addSec.setText("");
    }

    @FXML
    void buttonAdd_onClicked(ActionEvent event) {
        if (txtfieldPrice_addSec.getText().isEmpty()) {
            txtfieldPrice_addSec.setText("0");
        }

        if (txtfieldQuantity_addSec.getText().isEmpty()) {
            txtfieldQuantity_addSec.setText("0");
        }

        String product_name = txtfieldName_addSec.getText();
        double product_price = Math.round((Double.parseDouble(txtfieldPrice_addSec.getText())) * 100.0) / 100.0;
        int product_quantity = Integer.parseInt(txtfieldQuantity_addSec.getText());

        if(product_name.isEmpty() || product_price == 0){
            lblMessage_addsection.setText("*Invalid input");
            lblMessage_addsection.setStyle("-fx-text-fill:red");
            lblMessage_addsection.setVisible(true);
            return;
        }
        lblMessage_addsection.setVisible(true);
        lblMessage_addsection.setText("Added");
        lblMessage_addsection.setStyle("-fx-text-fill:green");
        ProductDAO.insertProduct(product_name, product_price, product_quantity);

        txtfieldName_addSec.setText("");
        txtfieldPrice_addSec.setText("");
        txtfieldQuantity_addSec.setText("");
        txtfieldName_addSec.requestFocus();
        loadProductData();
    }

    @FXML
    void txtfieldId_removeSec_onClicked(MouseEvent event) {
        lblMessage_removesection.setVisible(false);
        
    }

    @FXML
    void buttonRemove_onClicked(ActionEvent event) {
        if (txtfieldId_removeSec.getText().isEmpty()) {
            lblMessage_removesection.setVisible(true);
            txtfieldId_removeSec.requestFocus();
            return;
        }
        int product_id = Integer.parseInt(txtfieldId_removeSec.getText());
        
        if(!ProductDAO.isProductExist(product_id)){
            txtfieldId_removeSec.requestFocus();
            lblMessage_removesection.setText("*Invalid ID");
            lblMessage_removesection.setStyle("-fx-text-fill: red");
            lblMessage_removesection.setVisible(true);
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to remove?");
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() != ButtonType.OK) {
            txtfieldId_removeSec.setText("");
            txtfieldId_removeSec.requestFocus();
            return;
        }
        ProductDAO.deleteProduct(product_id);

        loadProductData();
        lblMessage_removesection.setVisible(true);
        lblMessage_removesection.setText("Removed");
        lblMessage_removesection.setStyle("-fx-text-fill:green");
        txtfieldId_removeSec.setText("");
        txtfieldId_removeSec.requestFocus();
    }

    @FXML
    void txtfieldId_updateSec_onCLicked(MouseEvent event) {
        lblMessage_addsection.setVisible(false);
        txtfieldName_updateSec.setText("");
        txtfieldPrice_updateSec.setText("");
        txtfieldQuantity_updateSec.setText("");
        txtfieldName_updateSec.setVisible(false);
        txtfieldPrice_updateSec.setVisible(false);
        txtfieldQuantity_updateSec.setVisible(false);
        txtfieldName_updateSec.setDisable(true);
        txtfieldPrice_updateSec.setDisable(true);
        txtfieldQuantity_updateSec.setDisable(true);
        lblMessage_updatesection.setVisible(false);
        chckboxName.setVisible(false);
        chckboxPrice.setVisible(false);
        chckboxQuantity.setVisible(false);
        chckboxName.setSelected(false);
        chckboxPrice.setSelected(false);
        chckboxQuantity.setSelected(false);
        buttonSearch.setVisible(true);
        buttonUpdate.setVisible(false);
    }   

    @FXML
    void buttonSearch_onClicked(ActionEvent event) {
        if (txtfieldId_updateSec.getText().isEmpty()) {
            lblMessage_updatesection.setText("*Invalid input");
            lblMessage_updatesection.setStyle("-fx-text-fill:red");
            lblMessage_updatesection.setVisible(true);
            return;
        }
        
        int productID = Integer.parseInt(txtfieldId_updateSec.getText());
        Product product;
        if ((product = ProductDAO.getProductById(productID)) == null){
            lblMessage_updatesection.setText("*Invalid ID");
            lblMessage_updatesection.setStyle("-fx-text-fill:red");
            lblMessage_updatesection.setVisible(true);
            txtfieldId_updateSec.requestFocus();
            return;
        }
        txtfieldName_updateSec.setText(product.getProductName());
        txtfieldPrice_updateSec.setText(String.valueOf(product.getPrice()));
        txtfieldQuantity_updateSec.setText(String.valueOf(product.getStockQuantity()));
        chckboxName.requestFocus();
        lblMessage_updatesection.setVisible(false);
        buttonSearch.setVisible(false);
        buttonUpdate.setVisible(true);
        chckboxName.setVisible(true);
        chckboxPrice.setVisible(true);
        chckboxQuantity.setVisible(true);
        txtfieldName_updateSec.setVisible(true);
        txtfieldPrice_updateSec.setVisible(true);
        txtfieldQuantity_updateSec.setVisible(true);
    }

    @FXML
    void buttonUpdate_onClicked(ActionEvent event) {
        int product_id = Integer.parseInt(txtfieldId_updateSec.getText());
        String name = txtfieldName_updateSec.getText();
        String price = txtfieldPrice_updateSec.getText();
        String quantity = txtfieldQuantity_updateSec.getText(); 


        if (!name.isEmpty()) {
            ProductDAO.updateProductName(product_id, name);
        }
        if(!price.isEmpty()){
            double actualPrice = Math.round((Double.parseDouble(price)) * 100.0) / 100.0;
            ProductDAO.updateProductPrice(product_id, actualPrice);
        }
        if (!quantity.isEmpty()) {
            ProductDAO.updateStockQuantity(product_id, Integer.parseInt(quantity));
        }
        lblMessage_updatesection.setVisible(true);
        lblMessage_updatesection.setText("Updated");
        lblMessage_updatesection.setStyle("-fx-text-fill:green");
        txtfieldId_updateSec.requestFocus();
        txtfieldId_updateSec.setText("");
        loadProductData();
        txtfieldName_updateSec.setVisible(false);
        txtfieldPrice_updateSec.setVisible(false);
        txtfieldQuantity_updateSec.setVisible(false);
        txtfieldName_updateSec.setDisable(true);
        txtfieldPrice_updateSec.setDisable(true);
        txtfieldQuantity_updateSec.setDisable(true);
        chckboxName.setSelected(false);
        txtfieldName_updateSec.setText("");
        txtfieldPrice_updateSec.setText("");
        txtfieldQuantity_updateSec.setText("");
        chckboxName.setSelected(false);
        chckboxPrice.setSelected(false);
        chckboxQuantity.setSelected(false);
        chckboxName.setVisible(false);
        chckboxPrice.setVisible(false);
        chckboxQuantity.setVisible(false);
        buttonSearch.setVisible(true);
        buttonUpdate.setVisible(false);
    }


    @FXML
    public void initialize() {
        if (user.getRole().equals("Cashier")) {
            borderPane.setBottom(null);
        }
        // Initialize columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));

        buttonSearch.disableProperty().bind(
          Bindings.createBooleanBinding(
                () -> txtfieldId_updateSec.getText().trim().isEmpty(),
                txtfieldId_updateSec.textProperty())  
        );

        // Use properties to track the initial values dynamically
        SimpleStringProperty initialName = new SimpleStringProperty();
        SimpleStringProperty initialPrice = new SimpleStringProperty();
        SimpleStringProperty initialQuantity = new SimpleStringProperty();

        // Capture initial values dynamically (e.g., after loading data)
        txtfieldName_updateSec.textProperty().addListener((_, oldVal, newVal) -> {
            if (initialName.get() == null || oldVal.isEmpty()) {
                initialName.set(newVal);
            }
        });

        txtfieldPrice_updateSec.textProperty().addListener((_, oldVal, newVal) -> {
            if (initialPrice.get() == null || oldVal.isEmpty()) {
                initialPrice.set(newVal);
            }
        });

        txtfieldQuantity_updateSec.textProperty().addListener((_, oldVal, newVal) -> {
            if (initialQuantity.get() == null || oldVal.isEmpty()) {
                initialQuantity.set(newVal);
            }
        });

        buttonUpdate.disableProperty().bind(
        Bindings.createBooleanBinding(
            () -> 
                  txtfieldName_updateSec.getText().equals(initialName.get()) &&
                  txtfieldPrice_updateSec.getText().equals(initialPrice.get()) &&
                  txtfieldQuantity_updateSec.getText().equals(initialQuantity.get()),
            
            txtfieldName_updateSec.textProperty(),
            txtfieldPrice_updateSec.textProperty(),
            txtfieldQuantity_updateSec.textProperty()
            )
        );

        buttonRemove.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> txtfieldId_removeSec.getText().trim().isEmpty(),
                txtfieldId_removeSec.textProperty()
            )
        );
        buttonAdd.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> txtfieldName_addSec.getText().trim().isEmpty() || 
                    txtfieldPrice_addSec.getText().trim().isEmpty() || 
                    txtfieldQuantity_addSec.getText().trim().isEmpty(),
                txtfieldName_addSec.textProperty(),
                txtfieldPrice_addSec.textProperty(),
                txtfieldQuantity_addSec.textProperty()
            )
        );

        txtfieldQuantity_addSec.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldQuantity_addSec.setText(oldValue);
            }
        });
        txtfieldQuantity_updateSec.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldQuantity_updateSec.setText(oldValue);
            }
        });
        txtfieldId_removeSec.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldId_removeSec.setText(oldValue);
            }
        });
        txtfieldId_updateSec.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldId_updateSec.setText(oldValue);
            }
        });
        txtfieldPrice_addSec.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) { 
                txtfieldPrice_addSec.setText(oldValue);
            }
        });
        txtfieldPrice_updateSec.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) { 
                txtfieldPrice_updateSec.setText(oldValue);
            }
        });

        // Load data from database
        loadProductData();
    }

    private void loadProductData() {
        productList = FXCollections.observableArrayList();

        String query = "SELECT * FROM Product";

        try (Connection conn = Inventory.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("product_id");
                String name = rs.getString("product_name");
                double price = rs.getDouble("price");
                int stockQuantity = rs.getInt("stock_quantity");

                productList.add(new Product(id, name, price, stockQuantity));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set data to TableView
        tableView.setItems(productList);
    }


}
