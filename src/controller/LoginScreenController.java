package controller;

import database.UserDAO;
import main.App;
import models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
public class LoginScreenController{

    
    
    @FXML
    private Button btnLogin;
    
    @FXML
    private Label errorText;
    
    @FXML
    private PasswordField passfieldPassword;

    @FXML
    private TextField txtfieldUsername;

    @FXML
    void buttonLogin_onClicked(ActionEvent event) {
        String username = txtfieldUsername.getText();
        String password = passfieldPassword.getText();
        
        User user = UserDAO.authenticateUser(username, password); 
        
        if(user != null){
            DashboardScreenController.user = user;
            App.switchScene("/view/DashboardScreen.fxml");
            errorText.setText("Login sucessfully");
            errorText.setStyle("-fx-text-fill:green");
        }else{
            if(username.isEmpty() && password.isEmpty()){
                errorText.setText("Invalid username and password");
                errorText.setStyle("-fx-text-fill: red");
                return;
            }else if(username.isEmpty()){
                errorText.setText("Invalid username");
                errorText.setStyle("-fx-text-fill: red");
                return;
            }else if(password.isEmpty()){
                errorText.setText("Invalid password");
                errorText.setStyle("-fx-text-fill: red");
                return;
            }
            errorText.setText("Incorrect password");
            errorText.setStyle("-fx-text-fill:red");
        }
    }
    @FXML
    void initialize(){
        txtfieldUsername.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[a-zA-Z0-9]*")) {  // Allow only letters and digits
                txtfieldUsername.setText(oldValue);
            }
        });
    }
}
