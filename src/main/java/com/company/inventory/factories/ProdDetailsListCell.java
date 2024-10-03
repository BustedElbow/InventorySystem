package com.company.inventory.factory;

import com.company.inventory.models.ProductIngredient;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class ProdDetailsListCell extends ListCell<ProductIngredient> {
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
