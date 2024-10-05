package com.company.inventory.factories;

import com.company.inventory.models.ProductIngredient;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ProdDetailsListCell extends ListCell<ProductIngredient> {
    private HBox hbox = new HBox();
    private HBox infoBox = new HBox(10);
    private StackPane itemPane = new StackPane();
    private StackPane infoPane = new StackPane();
    private StackPane unitPane = new StackPane();
    private Label itemId = new Label();
    private Label itemName = new Label();
    private Label neededQuantity = new Label();
    private Label unitMeasure = new Label();

    public ProdDetailsListCell() {
        itemId.setVisible(false);
        itemId.setManaged(false);

        infoBox.getChildren().addAll(neededQuantity, unitMeasure);

        itemPane.getChildren().addAll(itemId, itemName);
        infoPane.getChildren().add(infoBox);

        itemPane.setAlignment(Pos.CENTER_LEFT);
        infoPane.setAlignment(Pos.CENTER_RIGHT);
        infoBox.setAlignment(Pos.CENTER_RIGHT);

        itemPane.setPrefWidth(225);
        infoPane.setPrefWidth(225);

        hbox.setPrefWidth(450);
        hbox.getChildren().addAll(itemPane, infoPane);

        setGraphic(hbox);
    }

    protected void updateItem(ProductIngredient ingredient, boolean empty) {
        super.updateItem(ingredient, empty);
        if (empty || ingredient == null) {
            setGraphic(null);
            setText(null);
        } else {
            itemId.setText(Integer.toString(ingredient.getItemId()));
            itemName.setText(ingredient.getItemName());
            neededQuantity.setText(Double.toString(ingredient.getNeededQuantity()));
            unitMeasure.setText(ingredient.getUnitMeasure());
            setGraphic(hbox);
        }
    }
}
