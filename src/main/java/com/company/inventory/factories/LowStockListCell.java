package com.company.inventory.factories;

import com.company.inventory.models.Item;
import com.company.inventory.models.Sale;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class LowStockListCell extends ListCell<Item> {

    private HBox hbox = new HBox();
    private HBox infoBox = new HBox(5);
    private StackPane itemPane = new StackPane();
    private StackPane infoPane = new StackPane();
    private Label itemName = new Label();
    private Label remainingQuantity = new Label();
    private Label unitMeasure = new Label();

    public LowStockListCell() {
        itemPane.setAlignment(Pos.CENTER_LEFT);
        infoBox.setAlignment(Pos.CENTER_RIGHT);

        itemPane.getChildren().add(itemName);
        infoPane.getChildren().add(infoBox);

        infoBox.getChildren().addAll(remainingQuantity, unitMeasure);

        itemPane.setPrefWidth(330);
        infoPane.setPrefWidth(330);

        hbox.setPrefWidth(661);
        hbox.getChildren().addAll(itemPane, infoPane);
        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            itemName.setText(item.getName());
            if (item.getStock() <= 0) {
                remainingQuantity.setText("Out of Stock");
                remainingQuantity.setStyle("-fx-background-color: #f84545; -fx-font-family: 'Inter Semi Bold'; -fx-text-fill: #fefefe; -fx-padding: 4 4 4 4; -fx-background-radius: 4;");
                unitMeasure.setVisible(false);
                unitMeasure.setManaged(false);
            } else {
                remainingQuantity.setText(Double.toString(item.getStock()));
                unitMeasure.setText(item.getUnitMeasure());
            }

            setGraphic(hbox);
        }
    }
}
