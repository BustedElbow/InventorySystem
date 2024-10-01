package com.company.inventory.factory;

import com.company.inventory.controllers.AddProductController;
import com.company.inventory.models.Item;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
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
    private Button removeButton = new Button("<-");
    private Item item;

    public ProdUsedItemListCell(AddProductController controller) {
        this.controller = controller;

        actionPane.setAlignment(Pos.CENTER_LEFT);
        itemPane.setAlignment(Pos.CENTER_LEFT);
        measurePane.setAlignment(Pos.CENTER_RIGHT);

        actionPane.getChildren().add(removeButton);
        itemPane.getChildren().add(name);
        quantityPane.getChildren().add(quantity);
        measurePane.getChildren().add(measure);

        hbox.getChildren().addAll(actionPane, itemPane, quantityPane, measurePane);

        setGraphic(hbox);

        quantity.textProperty().addListener((obs, oldText, newText) -> {
            if (item != null) {
                try {
                    double newQuantity = Double.parseDouble(newText);
                    controller.updateItemQuantity(item, newQuantity); // Update the map
                } catch (NumberFormatException e) {
                    controller.updateItemQuantity(item, 0.0); // Default to 0 if input is invalid
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
                controller.addItemToList(item);
            });
            name.setText(item.getName());
            measure.setText(item.getUnitMeasure());
            setGraphic(hbox);
        }
    }

    public TextField getQuantity() {
        return quantity;
    }

    public Item getUsedItem() {
        return item;
    }
}
