package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
// import javafx.scene.layout.AnchorPane;
// import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
// import javafx.scene.layout.Pane;
import main.App;
import models.User;

public class DashboardScreenController {
    
    public static User user;

    @FXML
    private ImageView imageExit;

    @FXML
    private VBox containerButtons;

    @FXML
    private Button buttonProdManagement;

    @FXML
    private BorderPane dynamicPane;

    @FXML
    private Button buttonReportsAndAnalytics;

    @FXML
    private Button buttonUserManagement;

    @FXML
    private Button buttonPOS;

    @FXML
    void imageExit_onClicked(MouseEvent event) {
        App.switchScene("/view/LoginScreen.fxml");
    }

    void setPane(String path){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            BorderPane layout = fxmlLoader.load();
            dynamicPane.getChildren().setAll(layout);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void buttonProdManagement_onClicked(ActionEvent event) {
        
        setPane("/view/ProductManagementScreen.fxml");
    }

    @FXML
    void buttonReportsAndAnalytics_onClicked(ActionEvent event) {
        setPane("/view/ReportsAndAnalyticsScreen.fxml");
    }

    @FXML
    void buttonUserManagement_onClicked(ActionEvent event) {
        setPane("/view/UserManagementScreen.fxml");
    }

    @FXML
    void buttonPOS_onClicked(ActionEvent event) {
        setPane("/view/POSScreen.fxml");
    }

    @FXML
    void initialize(){

        ProductManagementScreenController.user = user;
        if (user.getRole().equals("Admin")) {
            setPane("/view/ProductManagementScreen.fxml");
            containerButtons.getChildren().remove(buttonPOS);
        }else{
            setPane("/view/POSScreen.fxml");
            containerButtons.getChildren().remove(buttonUserManagement);
            buttonPOS.requestFocus();
        }
    }
}

