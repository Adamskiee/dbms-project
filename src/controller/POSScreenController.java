package controller;

import java.util.HashMap;
import java.util.Optional;

import database.ProductDAO;
import database.SaleDAO;
import database.SaleItemDAO;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.SoldItem;
import models.Product;
import models.User;

public class POSScreenController {
    public static User user;

    @FXML
    private TextField txtfieldId;

    @FXML
    private TextField txtfieldName;

    @FXML
    private TextField txtfieldPrice;

    @FXML
    private TextField txtfieldQuantity;

    @FXML
    private TextField txtfieldStock;

    @FXML
    private TableColumn<SoldItem, String> nameColumn;

    @FXML
    private TableColumn<SoldItem, Double> priceColumn;

    @FXML
    private TableColumn<SoldItem, Integer> quantityColumn;

    @FXML
    private TableView<SoldItem> tableView;

     @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonCheckOut;

    @FXML
    private Button buttonRemove;

    @FXML
    private Button buttonSearch;

    @FXML
    private Label lblMessage_idSec;
    
    @FXML
    private Label lblMessage_quantitySec;
    
    @FXML
    private Label lblMessage_stockSec;
    
    @FXML
    private Label lblMessage_removeSec;
    
    @FXML
    private Label lblMessage_addSec;
    
    @FXML
    private Label lblMessage_checkoutSec;

    private ObservableList<SoldItem> saleItemList; 
    private HashMap<Integer, Product> productList;

    @FXML
    void txtfieldId_onClicked(MouseEvent event) {
        txtfieldName.setText("");
        txtfieldPrice.setText("");
        txtfieldStock.setText("");
        txtfieldQuantity.setDisable(true);
        tableView.getSelectionModel().clearSelection();
        lblMessage_idSec.setVisible(false);
        lblMessage_stockSec.setVisible(false);
        lblMessage_addSec.setVisible(false);
        lblMessage_removeSec.setVisible(false);
        lblMessage_checkoutSec.setVisible(false);
    }

    @FXML
    void txtfieldQuantity_onClicked(MouseEvent event){
        lblMessage_quantitySec.setVisible(false);
        lblMessage_addSec.setVisible(false);
        lblMessage_removeSec.setVisible(false);
        lblMessage_checkoutSec.setVisible(false);
    }
    
    @FXML
    void buttonAdd_onClicked(ActionEvent event) {
        Product product;
        int productId = Integer.parseInt(txtfieldId.getText());

        if (productList.containsKey(productId)) {
            product = productList.get(productId);
        }else{
            product = ProductDAO.getProductById(productId);
            productList.put(product.getProductID(), product);
        }

        int product_quantity = Integer.parseInt(txtfieldQuantity.getText());
        if (product_quantity > product.getStockQuantity()) {
            txtfieldQuantity.requestFocus();
            lblMessage_quantitySec.setVisible(true);
            return;
        }

        double subtotal = product.getPrice()*product_quantity;
        saleItemList.add(new SoldItem(product.getProductID(), product.getProductName(), product_quantity, subtotal));
        product.setStockQuantity(product.getStockQuantity() - product_quantity);
        tableView.setItems(saleItemList);
        
        lblMessage_quantitySec.setVisible(false);
        lblMessage_checkoutSec.setVisible(false);
        lblMessage_removeSec.setVisible(false);
        lblMessage_addSec.setVisible(true);
        txtfieldQuantity.setText("");
        txtfieldQuantity.setDisable(true);
        txtfieldId.setText("");
        txtfieldId.requestFocus();
        txtfieldName.setText("");
        txtfieldPrice.setText("");
        txtfieldStock.setText("");
    }

    @FXML
    void buttonCheckOut_onClicked(ActionEvent event) {
        if (saleItemList.isEmpty()) {
            return;
        }
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to checkout?");
        Optional <ButtonType> action = alert.showAndWait();
        if (action.get() != ButtonType.OK) {
            return;
        }

        double total_amount = 0;
        for (SoldItem soldItem : saleItemList) {
            total_amount += soldItem.getSubtotal();
        }
        
        int sale_id = SaleDAO.insertSale(user.getUser_id(), total_amount);
        for (SoldItem soldItem : saleItemList) {
            SaleItemDAO.insertSaleItem(sale_id, soldItem.getProductId(), soldItem.getQuantity(), soldItem.getSubtotal());
        }   
        
        saleItemList.clear();
        txtfieldId.requestFocus();
        lblMessage_checkoutSec.setVisible(true);
    }

    @FXML
    void buttonRemove_onClicked(ActionEvent evnt) {
        SoldItem selectedProduct = tableView.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to remove?");
            Optional <ButtonType> action = alert.showAndWait();
            if (action.get() != ButtonType.OK) {
                return;
            }

            saleItemList.remove(selectedProduct);
            
            Product product = productList.get(selectedProduct.getProductId());
            if(product.getStockQuantity() > selectedProduct.getQuantity()){
                product.setStockQuantity(product.getStockQuantity() + selectedProduct.getQuantity());
            }else{
                productList.remove(selectedProduct.getProductId());
            }
        } else {
            System.out.println("No product selected");
        }
        tableView.getSelectionModel().clearSelection();
        txtfieldId.requestFocus();

        lblMessage_removeSec.setVisible(true);
        lblMessage_quantitySec.setVisible(false);
        lblMessage_addSec.setVisible(false);
        lblMessage_checkoutSec.setVisible(false);
    }

    @FXML
    void buttonSearch_oncClicked(ActionEvent event) {
        int product_id = Integer.parseInt(txtfieldId.getText());
        Product product;

        lblMessage_addSec.setVisible(false);
        lblMessage_stockSec.setVisible(false);
        lblMessage_checkoutSec.setVisible(false);

        if(productList.containsKey(product_id)){
            product = productList.get(product_id);
        }
        else {
            if((product = ProductDAO.getProductById(product_id)) == null){
                txtfieldId.requestFocus();
                lblMessage_idSec.setVisible(true);
                
                return;
            }
        }
        
        txtfieldName.setText(product.getProductName());
        txtfieldPrice.setText(String.valueOf(product.getPrice()));
        txtfieldStock.setText(String.valueOf(product.getStockQuantity()));

        if(product.getStockQuantity() == 0){
            lblMessage_stockSec.setVisible(true);
            txtfieldId.requestFocus();
            return;
        }

        lblMessage_removeSec.setVisible(false);
        lblMessage_addSec.setVisible(false);
        lblMessage_idSec.setVisible(false);
        lblMessage_stockSec.setVisible(false);
        txtfieldQuantity.setDisable(false);
        txtfieldQuantity.requestFocus();
    }

    @FXML
    void pane_onClicked(){
        tableView.getSelectionModel().clearSelection();
    }

    @FXML
    public void initialize() {
        
        productList = new HashMap<>();
        saleItemList = FXCollections.observableArrayList();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        buttonSearch.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> txtfieldId.getText().trim().isEmpty(),
                txtfieldId.textProperty()
            )
        );

        buttonAdd.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> txtfieldQuantity.getText().trim().isEmpty(),
                txtfieldQuantity.textProperty()
            )
        );

        buttonRemove.disableProperty().bind(
            tableView.getSelectionModel().selectedItemProperty().isNull()
        );

        buttonCheckOut.disableProperty().bind(
            Bindings.isEmpty(saleItemList)
        );

        //Allow only numerical for textfields
        txtfieldId.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldId.setText(oldValue);
            }
        });
        txtfieldQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldQuantity.setText(oldValue);
            }
        });
    }
}
