package com.company.inventory.factories;

import com.company.inventory.controllers.AddProductController;
import com.company.inventory.models.Item;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ProdItemListCell extends ListCell<Item> {
    private HBox hbox = new HBox();
    private StackPane itemPane = new StackPane();
    private StackPane actionPane = new StackPane();
    private Label name = new Label();
    private Button addButton = new Button();
    private AddProductController controller;
    public ProdItemListCell(AddProductController controller) {
        this.controller = controller;

        itemPane.setAlignment(Pos.CENTER_LEFT);
        actionPane.setAlignment(Pos.CENTER_RIGHT);

        Image image = new Image(getClass().getResource("/icons/add100-black.png").toString());
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        addButton.setGraphic(imageView);
        addButton.setStyle("-fx-background-color: #ee8850; -fx-background-radius: 4; -fx-cursor: hand;");

        itemPane.getChildren().add(name);
        actionPane.getChildren().add(addButton);

        itemPane.setPrefWidth(224);
        actionPane.setPrefWidth(190);

        hbox.setPrefWidth(364);
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
