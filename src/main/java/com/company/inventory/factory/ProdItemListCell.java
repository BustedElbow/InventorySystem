package com.company.inventory.factory;

import com.company.inventory.controllers.AddProductController;
import com.company.inventory.models.Item;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ProdItemListCell extends ListCell<Item> {
    private HBox hbox = new HBox();
    private StackPane itemPane = new StackPane();
    private StackPane actionPane = new StackPane();
    private Label name = new Label();
    private Button addButton = new Button("->");
    private AddProductController controller;
    public ProdItemListCell(AddProductController controller) {
        this.controller = controller;

        itemPane.setAlignment(Pos.CENTER_LEFT);
        actionPane.setAlignment(Pos.CENTER_RIGHT);

        itemPane.getChildren().add(name);
        actionPane.getChildren().add(addButton);

        itemPane.setPrefWidth(100);
        actionPane.setPrefWidth(100);

        hbox.setPrefWidth(200);
        hbox.getChildren().addAll(itemPane, actionPane);

        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {
            setGraphic(null);
        } else {
            name.setText(item.getName());
            addButton.setOnAction(e -> {
                controller.addItemToProduct(item);
                controller.removeItemFromList(item);
            });
            setGraphic(hbox);
        }
    }

}
