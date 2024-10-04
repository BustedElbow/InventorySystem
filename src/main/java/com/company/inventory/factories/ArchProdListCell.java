package com.company.inventory.factories;

import com.company.inventory.controllers.EditProductController;
import com.company.inventory.models.Product;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ArchProdListCell extends ListCell<Product> {
    private HBox hbox = new HBox();
    private StackPane productPane = new StackPane();
    private StackPane pricePane = new StackPane();
    private Label productId = new Label();
    private Label productName = new Label();
    private Label productPrice = new Label();

    public ArchProdListCell() {

        productPane.setAlignment(Pos.CENTER_LEFT);
        pricePane.setAlignment(Pos.CENTER_RIGHT);

        productId.setVisible(false);
        productId.setManaged(false);

        productPane.getChildren().addAll(productId, productName);
        pricePane.getChildren().add(productPrice);

        productPane.setPrefWidth(230);
        pricePane.setPrefWidth(230);

        hbox.setPadding(new Insets(4, 0 , 4, 0));
        hbox.setPrefWidth(518);
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
            productPrice.setText(Double.toString(product.getProductPrice()));

            setGraphic(hbox);
        }
    }
}
