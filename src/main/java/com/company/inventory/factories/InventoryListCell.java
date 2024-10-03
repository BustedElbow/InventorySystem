package com.company.inventory.factories;

import com.company.inventory.models.Item;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class InventoryListCell extends ListCell<Item> {
    private HBox hbox = new HBox();
    private Label id = new Label();
    private Label name = new Label();
    private Label unit = new Label();
    private Label level = new Label();
    private Label stock = new Label();
    private Button editButton = new Button("Edit");
    private StackPane unitPane = new StackPane();
    private StackPane stockPane = new StackPane();
    private StackPane namePane = new StackPane();
    private StackPane actionPane = new StackPane();
    private StackPane levelPane = new StackPane();

    public InventoryListCell() {

        editButton.setStyle("-fx-background-radius: 8; -fx-background-color: #ee8850; -fx-text-fill: #1e1e1e; -fx-padding: 8 12 8 12; -fx-font-family: 'Inter Semi Bold'; -fx-font-size: 16; -fx-cursor: hand;");

        namePane.setAlignment(Pos.CENTER_LEFT);
        actionPane.setAlignment(Pos.CENTER_RIGHT);

        id.setVisible(false);
        id.setManaged(false);

        namePane.getChildren().addAll(id, name);
        stockPane.getChildren().add(stock);
        levelPane.getChildren().add(level);
        unitPane.getChildren().add(unit);
        actionPane.getChildren().add(editButton);

        levelPane.setPrefWidth(209);
        namePane.setPrefWidth(209);
        stockPane.setPrefWidth(209);
        unitPane.setPrefWidth(209);
        actionPane.setPrefWidth(209);

        hbox.setPrefWidth(1000);
        hbox.getChildren().addAll(namePane, stockPane, levelPane, unitPane, actionPane);

        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            id.setText(Integer.toString(item.getId()));
            name.setText(item.getName());
            unit.setText(String.valueOf(item.getUnitMeasure()));
            stock.setText(String.valueOf(item.getStock()));
            level.setText(String.valueOf(item.getReorderLevel()));
            editButton.setOnAction(e -> {
                System.out.println("Edit clicked for " + item.getName());
            });
            setGraphic(hbox);
        }
    }
}
