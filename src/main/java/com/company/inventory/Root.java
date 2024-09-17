package com.company.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Root extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Image icon = new Image(getClass().getResource("/images/um_logo.jpg").toString());

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/root_layout.fxml"));
        Scene scene = new Scene(root);

        stage.getIcons().add(icon);
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
