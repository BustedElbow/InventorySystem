package com.company.inventory.factories;

import com.company.inventory.controllers.NewSaleController;
import com.company.inventory.models.Product;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class SaleSelectProdListCell extends ListCell<Product> {
    private HBox hbox = new HBox();
    private StackPane actionPane = new StackPane();
    private StackPane productPane = new StackPane();
    private StackPane quantityPane = new StackPane();
    private StackPane pricePane = new StackPane();
    private Label productName = new Label();
    private Label price = new Label();
    private TextField quantityField = new TextField();
    private Button removeButton = new Button();
    private NewSaleController controller;
    public SaleSelectProdListCell(NewSaleController controller) {
        this.controller = controller;

        quantityField.setText("1");
        quantityField.setStyle("-fx-text-fill: #1e1e1e; " +
                "-fx-font-family: \"Inter Semi Bold\"; " +
                "-fx-font-size: 16; " +
                "-fx-border-color: #b8b8b8; " +
                "-fx-border-radius: 8; " +
                "-fx-background-radius: 8;");

        Image image = new Image(getClass().getResource("/icons/minus100-black.png").toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(20);
        imageView.setFitHeight(20);

        removeButton.setGraphic(imageView);
        removeButton.setStyle("-fx-background-color: #ee8850; -fx-background-radius: 4; -fx-cursor: hand;");

        actionPane.setAlignment(Pos.CENTER_LEFT);
        productPane.setAlignment(Pos.CENTER_LEFT);
        pricePane.setAlignment(Pos.CENTER_RIGHT);

        actionPane.getChildren().add(removeButton);
        productPane.getChildren().add(productName);
        quantityPane.getChildren().add(quantityField);
        pricePane.getChildren().add(price);

        actionPane.setPrefWidth(67);
        productPane.setPrefWidth(200);
        quantityPane.setPrefWidth(89);
        pricePane.setPrefWidth(96);

        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(newVal);
                    controller.updateProductQuantity(getItem(), quantity);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid value");
                }
            }
        });

        hbox.setPrefWidth(458);
        hbox.getChildren().addAll(actionPane, productPane, quantityPane, pricePane);
        setGraphic(hbox);

    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if (empty || product == null) {
            setGraphic(null);
        } else {
            price.setText(Double.toString(product.getProductPrice()));
            productName.setText(product.getProductName());
            removeButton.setOnAction(e -> {
                controller.removeProductFromSale(getItem());
            });

            setGraphic(hbox);
        }
    }
}
