package com.company.inventory.factory;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ProdDetailsListCell extends ListCell<String> {
    private HBox hbox = new HBox();
    private StackPane itemPane = new StackPane();
    private StackPane quantityPane = new StackPane();
    private StackPane unitPane = new StackPane();
    private Label itemId = new Label();
    private Label itemName = new Label();
    private Label neededQuantity = new Label();
    private Label unitMeasure = new Label();

    public ProdDetailsListCell() {
        itemId.setVisible(false);
        itemId.setManaged(false);

        itemPane.getChildren().addAll(itemId, itemName);
        quantityPane.getChildren().add(neededQuantity);
        unitPane.getChildren().add(unitMeasure);

        hbox.setPrefWidth(400);
        hbox.getChildren().addAll(itemPane, quantityPane, unitPane);

        setGraphic(hbox);
    }

    protected void updateItem(String itemDetail, boolean empty) {
        super.updateItem(itemDetail, empty);
        if (empty || itemDetail == null) {
            setGraphic(null);
        } else {
            String[] details = itemDetail.split(" ");
            if (details.length == 4) {
                itemId.setText(details[0]);
                itemName.setText(details[1]);
                neededQuantity.setText(details[2]);
                unitMeasure.setText(details[3]);
            }
            setGraphic(hbox);
        }
    }
}
