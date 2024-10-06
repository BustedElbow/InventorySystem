package com.company.inventory.factories;

import com.company.inventory.models.SaleDetails;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class SaleDetailsListCell extends ListCell<SaleDetails> {
    private HBox hbox = new HBox();
    private HBox infoBox = new HBox(5);
    private StackPane itemPane = new StackPane();
    private StackPane infoPane = new StackPane();
    private StackPane unitPane = new StackPane();
    private Label itemId = new Label();
    private Label productName = new Label();
    private Label neededQuantity = new Label();
    private Label productPrice = new Label();

    public SaleDetailsListCell() {
        itemId.setVisible(false);
        itemId.setManaged(false);

        infoBox.getChildren().addAll(neededQuantity, productPrice);

        itemPane.getChildren().addAll(itemId, productName);
        infoPane.getChildren().add(infoBox);

        itemPane.setAlignment(Pos.CENTER_LEFT);
        infoPane.setAlignment(Pos.CENTER_RIGHT);
        infoBox.setAlignment(Pos.CENTER_RIGHT);

        itemPane.setPrefWidth(175);
        infoPane.setPrefWidth(175);

        hbox.setPrefWidth(300);
        hbox.getChildren().addAll(itemPane, infoPane);

        setGraphic(hbox);
    }

    protected void updateItem(SaleDetails product, boolean empty) {
        super.updateItem(product, empty);
        if (empty || product == null) {
            setGraphic(null);
            setText(null);
        } else {
            productName.setText(product.getProductName());
            neededQuantity.setText(product.getProductQuantity() + " x ");
            productPrice.setText("Php " + product.getProductPrice());
            setGraphic(hbox);
        }
    }
}
