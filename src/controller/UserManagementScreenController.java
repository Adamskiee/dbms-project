package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import database.Inventory;
import database.UserDAO;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.User;

public class UserManagementScreenController {
    
    @FXML
    private ComboBox<String> comboxRole_addSec = new ComboBox<>();
    
    @FXML
    private ComboBox<String> comboxRole_updateSec = new ComboBox<>();

    @FXML
    private TextField txtfieldId_updateSec;

    @FXML
    private TextField txtfieldName_addSec;

    @FXML
    private TextField txtfieldName_updateSec;

    @FXML
    private TextField txtfieldPass_addSec;

    @FXML
    private TextField txtfieldPass_updateSec;

    @FXML
    private TextField txtfieldId_removeSec;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> idColumn;

    @FXML
    private TableColumn<User, String> nameColumn;

    @FXML
    private TableColumn<User, String> passColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    @FXML
    private Label lblMessage_removeSec;

    @FXML
    private Label lblMessage_updateSec;
    
    @FXML
    private Label lblMessage_addSec;

    @FXML
    private CheckBox chckboxName;

    @FXML
    private CheckBox chckboxPass;

    @FXML
    private CheckBox chckboxRole;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonRemove;

    @FXML
    private Button buttonSearch;

    @FXML
    private Button buttonUpdate;

    private ObservableList<User> userList;
    
    @FXML
    void buttonAdd_onClicked(ActionEvent event) {
        String username = txtfieldName_addSec.getText();
        String password = txtfieldPass_addSec.getText();
        String role = comboxRole_addSec.getValue();

        if (UserDAO.authenticateUsername(username)) {
            UserDAO.insertUser(username, password, role);
        }else{
            lblMessage_addSec.setText("Username exists");
            lblMessage_addSec.setStyle("-fx-text-fill: red");
            lblMessage_addSec.setVisible(true);
            txtfieldName_addSec.requestFocus();
            System.out.println("Username exists");
            return;
        }

        lblMessage_addSec.setVisible(true);
        lblMessage_addSec.setText("Added");
        lblMessage_addSec.setStyle("-fx-text-fill:green");
        txtfieldName_addSec.requestFocus();
        txtfieldName_addSec.setText("");
        txtfieldPass_addSec.setText("");
        loadUserData();
    }

    @FXML
    void buttonRemove_onClicked(ActionEvent event) {
        int userId = Integer.parseInt(txtfieldId_removeSec.getText());

        if (UserDAO.isUserExist(userId)) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure to delete?");
            Optional <ButtonType> action = alert.showAndWait();
            if (action.get() != ButtonType.OK) {
                txtfieldId_removeSec.setText("");
                txtfieldId_removeSec.requestFocus();
                return;
            }
            UserDAO.removeUser(userId);
            lblMessage_removeSec.setText("Removed");
            lblMessage_removeSec.setStyle("-fx-text-fill:green");
            lblMessage_removeSec.setVisible(true);
        }else{
            lblMessage_removeSec.setText("*Invalid ID");
            lblMessage_removeSec.setStyle("-fx-text-fill:red");
            lblMessage_removeSec.setVisible(true);
        }
        loadUserData();

        txtfieldId_removeSec.setText("");
    }

    @FXML
    void buttonSearch_onClicked(ActionEvent event) {
        int userID = Integer.parseInt(txtfieldId_updateSec.getText());
        User user;
        if((user = UserDAO.getUser(userID)) == null){
            txtfieldId_updateSec.requestFocus();
            lblMessage_updateSec.setText("*Invalid ID");
            lblMessage_updateSec.setStyle("-fx-text-fill:red");
            lblMessage_updateSec.setVisible(true);
            return;
        }

        txtfieldName_updateSec.setText(user.getUsername());
        txtfieldPass_updateSec.setText(user.getPassword());
        comboxRole_updateSec.setValue(user.getRole());

        lblMessage_updateSec.setVisible(false);
        chckboxName.requestFocus();
        buttonSearch.setVisible(false);
        buttonUpdate.setVisible(true);
        chckboxName.setVisible(true);
        chckboxRole.setVisible(true);
        chckboxPass.setVisible(true);
        txtfieldName_updateSec.setVisible(true);
        txtfieldPass_updateSec.setVisible(true);
        comboxRole_updateSec.setVisible(true);
    }   
    
    @FXML
    void txtfieldId_updateSec_onClicked(MouseEvent event){
        lblMessage_updateSec.setVisible(false);
        buttonSearch.setVisible(true);
        buttonUpdate.setVisible(false);
        chckboxName.setVisible(false);
        chckboxRole.setVisible(false);
        chckboxPass.setVisible(false);
        chckboxName.setSelected(false);
        chckboxPass.setSelected(false);
        chckboxRole.setSelected(false);
        txtfieldName_updateSec.setVisible(false);
        txtfieldPass_updateSec.setVisible(false);
        txtfieldName_updateSec.setText("");
        txtfieldPass_updateSec.setText("");
        txtfieldName_updateSec.setDisable(true);
        txtfieldPass_updateSec.setDisable(true);
        comboxRole_updateSec.setVisible(false);
        // buttonUpdate.setD
    }
    @FXML
    void txtfieldId_removeSec_onClicked(MouseEvent event) {
        lblMessage_removeSec.setVisible(false);
    }
    @FXML
    void txtfieldName_addSec_onClicked(MouseEvent event) {
        lblMessage_addSec.setVisible(false);
    }
    @FXML
    void buttonUpdate_onClicked(ActionEvent event) {
        int userId = Integer.parseInt(txtfieldId_updateSec.getText());
        String username = txtfieldName_updateSec.getText();
        String password = txtfieldPass_updateSec.getText();
        String role = "";

        if (chckboxRole.isSelected()) {
            role = comboxRole_updateSec.getValue();
        }
        
        if(chckboxName.isSelected()){
            if (UserDAO.authenticateUsername(username) == false) {
                lblMessage_updateSec.setVisible(true);
                lblMessage_updateSec.setText("*Username");
                lblMessage_updateSec.setStyle("-fx-text-fill:red");
                return;
            }
            UserDAO.updateUsername(username, userId);
        }

        if(chckboxPass.isSelected()){
            UserDAO.updatePassword(password, userId);
        }   
        if(chckboxRole.isSelected()){
            UserDAO.updateRole(role, userId);
        }

        loadUserData();

        lblMessage_updateSec.setText("Updated");
        lblMessage_updateSec.setVisible(true);
        lblMessage_updateSec.setStyle("-fx-text-fill: green");
        txtfieldId_updateSec.setText("");
        txtfieldId_updateSec.requestFocus();
        buttonSearch.setVisible(true);
        buttonUpdate.setVisible(false);
        chckboxName.setVisible(false);
        chckboxRole.setVisible(false);
        chckboxPass.setVisible(false);
        chckboxName.setSelected(false);
        chckboxPass.setSelected(false);
        chckboxRole.setSelected(false);
        txtfieldName_updateSec.setVisible(false);
        txtfieldPass_updateSec.setVisible(false);
        txtfieldName_updateSec.setText("");
        txtfieldPass_updateSec.setText("");
        txtfieldName_updateSec.setDisable(true);
        txtfieldPass_updateSec.setDisable(true);
        comboxRole_updateSec.setDisable(true);
        comboxRole_updateSec.setVisible(false);
        comboxRole_updateSec.setValue(null);
    }

    @FXML
    void chckboxName_onClicked(ActionEvent event) {
        if (chckboxName.isSelected()) {
            txtfieldName_updateSec.setDisable(false);
        }else{
            txtfieldName_updateSec.setDisable(true);
        }
    }

    @FXML
    void chckboxPass_onClicked(ActionEvent event) {
        if (chckboxPass.isSelected()) {
            txtfieldPass_updateSec.setDisable(false);
        }else{
            txtfieldPass_updateSec.setDisable(true);
        }
    }

    @FXML
    void chckboxRole_onClicked(ActionEvent event) {
        if (chckboxRole.isSelected()) {
            comboxRole_updateSec.setDisable(false);
        }else{
            comboxRole_updateSec.setDisable(true);
        }
    }

    // boolean showConfirmationDialog() {
    //     // Create an Alert of type Confirmation
    //     Alert alert = new Alert(AlertType.CONFIRMATION);
    //     alert.setTitle("Confirmation");
    //     alert.setHeaderText("Are you sure?");
    //     alert.setContentText("Do you want to proceed with this action?");

    //     boolean choice = true;
    //     // Show the dialog and wait for a response
    //     alert.showAndWait().ifPresent(response -> {
    //         if (response == javafx.scene.control.ButtonType.OK) {
    //             System.out.println("User confirmed the action.");
    //             choice = true;
    //         } else {
    //             System.out.println("User canceled the action.");
    //             choice = false;
    //         }
    //     });
    //     return choice;
    // }

    @FXML
    void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        comboxRole_addSec.getItems().add("Admin");
        comboxRole_addSec.getItems().add("Cashier");
        comboxRole_updateSec.getItems().add("Admin");
        comboxRole_updateSec.getItems().add("Cashier");
        comboxRole_addSec.setValue("Cashier");
        
       buttonAdd.disableProperty().bind(
          Bindings.createBooleanBinding(
                () -> txtfieldName_addSec.getText().trim().isEmpty() ||
                txtfieldPass_addSec.getText().trim().isEmpty(),
                txtfieldName_addSec.textProperty(),
                txtfieldPass_addSec.textProperty())
        );

        buttonSearch.disableProperty().bind(
            Bindings.createBooleanBinding(
                ()-> txtfieldId_updateSec.getText().trim().isEmpty(),
                txtfieldId_updateSec.textProperty())
        );
            // Use properties to track the initial values dynamically
        SimpleStringProperty initialName = new SimpleStringProperty();
        SimpleStringProperty initialPass = new SimpleStringProperty();
        SimpleStringProperty initialRole = new SimpleStringProperty();

        // Capture initial values dynamically for text fields
        txtfieldName_updateSec.textProperty().addListener((obs, oldVal, newVal) -> {
            if (initialName.get() == null || oldVal.isEmpty()) {
                initialName.set(newVal);
            }
        });

        txtfieldPass_updateSec.textProperty().addListener((obs, oldVal, newVal) -> {
            if (initialPass.get() == null || oldVal.isEmpty()) {
                initialPass.set(newVal);
            }
        });

        // Capture initial value dynamically for the ComboBox
        comboxRole_updateSec.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (initialRole.get() == null || oldVal == null) {
                initialRole.set(newVal);
            }
        });

        // Bind the button's disable property
        buttonUpdate.disableProperty().bind(Bindings.createBooleanBinding(
                () -> txtfieldName_updateSec.getText().equals(initialName.get()) &&
                    txtfieldPass_updateSec.getText().equals(initialPass.get()) &&
                    comboxRole_updateSec.getValue() != null &&
                    comboxRole_updateSec.getValue().equals(initialRole.get()),
                txtfieldName_updateSec.textProperty(),
                txtfieldPass_updateSec.textProperty(),
                comboxRole_updateSec.valueProperty()
            )
        );


        // buttonUpdate.disableProperty().bind(
        //     Bindings.createBooleanBinding(
        //         ()-> txtfieldName_updateSec.getText().trim().isEmpty() &&
        //         txtfieldPass_updateSec.getText().trim().isEmpty() &&
        //         comboxRole_updateSec.getValue() == null,
        //         txtfieldName_updateSec.textProperty(),
        //         txtfieldPass_updateSec.textProperty(),
        //         comboxRole_updateSec.valueProperty())
        // );

        buttonRemove.disableProperty().bind(
            Bindings.createBooleanBinding(
                ()-> txtfieldId_removeSec.getText().trim().isEmpty(),
                txtfieldId_removeSec.textProperty())
        );

        //Allow only numerical for textfields
        txtfieldId_removeSec.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldId_removeSec.setText(oldValue);
            }
        });
        txtfieldId_updateSec.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { 
                txtfieldId_updateSec.setText(oldValue);
            }
        });

        loadUserData();
    }
    
    private void loadUserData() {
        userList = FXCollections.observableArrayList();

        String query = "SELECT * FROM User";

        try (Connection conn = Inventory.connect();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("user_id");
                String name = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");

                userList.add(new User(id, name, password, role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set data to TableView
        tableView.setItems(userList);
    }
}
