package com.company.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception{
        Image icon = new Image(getClass().getResource("/images/um_logo.jpg").toString());

        FXMLLoader fxml = new FXMLLoader(Main.class.getResource("/fxml/sandbox.fxml"));
        Scene scene = new Scene(fxml.load());

        stage.getIcons().add(icon);
        stage.setTitle("Inventory System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
