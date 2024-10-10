package com.company.inventory.factories;
import com.company.inventory.models.InventoryLog;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InventoryLogListCell extends ListCell<InventoryLog> {
    private HBox hbox = new HBox(10);
    private HBox hbox1 = new HBox(5);
    private HBox hbox2 = new HBox(5);
    private HBox hbox3 = new HBox(5);
    private StackPane stackPane1 = new StackPane(); // Item ID and name
    private StackPane stackPane2 = new StackPane(); // Change quantity, previous quantity, new quantity
    private StackPane stackPane3 = new StackPane(); // Reference ID and status
    private StackPane stackPane4 = new StackPane(); // Date

    private Label itemId = new Label();
    private Label itemName = new Label();
    private Label currentQuantity = new Label();
    private Label referenceStatus = new Label();
    private Label dateLabel = new Label();

    public InventoryLogListCell() {

        stackPane1.getChildren().add(hbox1);
        stackPane2.getChildren().add(hbox2);
        stackPane3.getChildren().add(hbox3);

        hbox1.setAlignment(Pos.CENTER_LEFT);
        stackPane4.setAlignment(Pos.CENTER_RIGHT);

        hbox1.getChildren().addAll(itemId, itemName);
        hbox2.getChildren().add(currentQuantity);
        hbox3.getChildren().add(referenceStatus);

        stackPane1.setPrefWidth(261.25);
        stackPane2.setPrefWidth(261.25);
        stackPane3.setPrefWidth(261.25);
        stackPane4.setPrefWidth(250);

        hbox.setPrefWidth(1035);
        hbox.getChildren().addAll(stackPane1, stackPane2, stackPane3, stackPane4);
        setGraphic(hbox);
    }

    @Override
    protected void updateItem(InventoryLog log, boolean empty) {
        super.updateItem(log, empty);
        if (empty || log == null) {
            setGraphic(null);
        } else {
            itemId.setText("ID " + log.getItemId() + " - ");
            itemName.setText(log.getItemName());
            currentQuantity.setText(Double.toString(log.getNewQuantity()));
            referenceStatus.setText(log.getChangeType());

            if (log.getChangeType().equals("DELETED")) {
                referenceStatus.setStyle("-fx-text-fill: #f84545");
            } else if (log.getChangeType().equals("RESTORED")) {
                referenceStatus.setStyle("-fx-text-fill: green");
            } else {
                referenceStatus.setStyle("-fx-text-fill: #1e1e1e");
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            LocalDateTime timestamp = LocalDateTime.parse(log.getTimestamp());
            dateLabel.setText(timestamp.format(formatter));

            stackPane4.getChildren().clear();
            stackPane4.getChildren().add(dateLabel);

            setGraphic(hbox);
        }
    }
}