package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application{
    private static Stage primaryStage;
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        primaryStage.setOnShown(_ -> {
            centerStage();
        });
        switchScene("/view/LoginScreen.fxml");
        primaryStage.show();
    }

    public static void switchScene(String path){
        try {
            Parent loader = FXMLLoader.load(App.class.getResource(path));
            Scene scene = new Scene(loader);
            primaryStage.setScene(scene);
            
            centerStage();

            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void centerStage(){
        // Get screen bounds
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Get stage dimensions
        double stageWidth = primaryStage.getWidth();
        double stageHeight = primaryStage.getHeight();

        // Calculate center position
        double centerX = (screenBounds.getWidth() - stageWidth) / 2 + screenBounds.getMinX();
        double centerY = (screenBounds.getHeight() - stageHeight) / 2 + screenBounds.getMinY();

        // Set stage position
        primaryStage.setX(centerX);
        primaryStage.setY(centerY);
    }
}
