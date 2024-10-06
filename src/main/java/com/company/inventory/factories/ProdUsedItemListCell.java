package com.company.inventory.factories;

import com.company.inventory.controllers.AddProductController;
import com.company.inventory.models.Item;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

public class ProdUsedItemListCell extends ListCell<Item> {
    private AddProductController controller;
    private HBox hbox = new HBox();
    private StackPane actionPane = new StackPane();
    private StackPane itemPane = new StackPane();
    private StackPane quantityPane = new StackPane();
    private StackPane measurePane = new StackPane();
    private Label name = new Label();
    private Label measure = new Label();
    private TextField quantity = new TextField();
    private Button removeButton = new Button();
    private Item item;

    public ProdUsedItemListCell(AddProductController controller) {
        this.controller = controller;

        actionPane.setAlignment(Pos.CENTER_LEFT);
        itemPane.setAlignment(Pos.CENTER_LEFT);
        measurePane.setAlignment(Pos.CENTER_RIGHT);

        Image image = new Image(getClass().getResource("/icons/minus100-black.png").toString());
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        removeButton.setGraphic(imageView);
        removeButton.setStyle("-fx-background-color: #ee8850; -fx-background-radius: 4; -fx-cursor: hand;");

        quantity.setStyle("-fx-text-fill: #1e1e1e;\n" +
                "    -fx-font-family: \"Inter Semi Bold\";\n" +
                "    -fx-font-size: 16;\n" +
                "    -fx-border-color: #b8b8b8;\n" +
                "    -fx-border-radius: 8;\n" +
                "    -fx-background-radius: 8;");

        actionPane.getChildren().add(removeButton);
        itemPane.getChildren().add(name);
        quantityPane.getChildren().add(quantity);
        measurePane.getChildren().add(measure);

        actionPane.setPrefWidth(67);
        itemPane.setPrefWidth(200);
        quantityPane.setPrefWidth(89);
        measurePane.setPrefWidth(96);

        hbox.setPrefWidth(458);
        hbox.getChildren().addAll(actionPane, itemPane, quantityPane, measurePane);

        setGraphic(hbox);

        quantity.textProperty().addListener((obs, oldText, newText) -> {
            if (item != null) {
                try {
                    double newQuantity = Double.parseDouble(newText);
                    controller.updateItemQuantity(item, newQuantity);
                } catch (NumberFormatException e) {
                    controller.updateItemQuantity(item, 0.0);
                }
            }
        });
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        this.item = item;

        if(empty || item == null) {
            setGraphic(null);
        } else {
            removeButton.setOnAction(e -> {
                controller.removeItemFromProduct(item);
            });
            name.setText(item.getName());
            measure.setText(item.getUnitMeasure());
            quantity.setText(Double.toString(1));
            setGraphic(hbox);
        }
    }
}
