package com.company.inventory.factories;

import com.company.inventory.controllers.EditProductController;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.*;
import com.company.inventory.models.Product;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class ProductListCell extends ListCell<Product> {
    private HBox hbox = new HBox();
    private HBox infoBox = new HBox(5);
    private StackPane productPane = new StackPane();
    private StackPane pricePane = new StackPane();
    private Label productId = new Label();
    private Label productName = new Label();
    private Label productPrice = new Label();
    private Button editButton = new Button();

    public ProductListCell() {

        Image image = new Image(getClass().getResource("/icons/edit100-black.png").toString());
        ImageView editImg = new ImageView(image);

        editImg.setFitHeight(20);
        editImg.setFitWidth(20);

        editButton.setGraphic(editImg);
        editButton.setStyle("-fx-background-radius: 4; -fx-background-color: #fefefe; -fx-padding: 4 4 4 4; -fx-cursor: hand; -fx-border-radius: 4; -fx-border-color: #929292");

        productPane.setAlignment(Pos.CENTER_LEFT);
        pricePane.setAlignment(Pos.CENTER_RIGHT);
        infoBox.setAlignment(Pos.CENTER_RIGHT);

        productId.setVisible(false);
        productId.setManaged(false);

        productPane.getChildren().addAll(productId, productName);
        pricePane.getChildren().add(infoBox);

        infoBox.getChildren().addAll(productPrice, editButton);

        productPane.setPrefWidth(250);
        pricePane.setPrefWidth(250);

//        hbox.setPadding(new Insets(2, 0 , 2, 0));
        hbox.setPrefWidth(503);
        hbox.getChildren().addAll(productPane, pricePane);

        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if(empty || product == null) {
            setGraphic(null);
        } else {
            productName.setText(product.getProductName());
            productId.setText(Integer.toString(product.getProductId()));
            productPrice.setText("Php " + product.getProductPrice());
            editButton.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editProduct.fxml"));
                    Parent root = loader.load();

                    EditProductController editProductController = loader.getController();
                    editProductController.setProduct(getItem());

                    Stage stage = new Stage();
                    stage.setTitle("Edit Product");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();

                    getListView().refresh();

                } catch (IOException er) {
                    er.printStackTrace();
                }
            });
            setGraphic(hbox);
        }
    }
}
