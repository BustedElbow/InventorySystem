package com.company.inventory.factories;

import com.company.inventory.controllers.EditProductController;
import com.company.inventory.models.ProductIngredient;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class EditProdUsedItemListCell extends ListCell<ProductIngredient> {
    private EditProductController controller;
    private HBox hbox = new HBox();
    private StackPane actionPane = new StackPane();
    private StackPane itemPane = new StackPane();
    private StackPane quantityPane = new StackPane();
    private StackPane measurePane = new StackPane();
    private Label name = new Label();
    private Label measure = new Label();
    private TextField quantity = new TextField();
    private Button removeButton = new Button();
    private ProductIngredient ingredient;

    public EditProdUsedItemListCell(EditProductController controller) {
        this.controller = controller;

        actionPane.setAlignment(Pos.CENTER_LEFT);
        itemPane.setAlignment(Pos.CENTER_LEFT);
        measurePane.setAlignment(Pos.CENTER_RIGHT);

        Image image = new Image(getClass().getResource("/icons/minus100-black.png").toString());
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        removeButton.setGraphic(imageView);
        removeButton.setStyle("-fx-background-color: #ee8850; -fx-background-radius: 4; -fx-cursor: hand;");

        quantity.setStyle("-fx-text-fill: #1e1e1e;\n" +
                "    -fx-font-family: \"Inter Semi Bold\";\n" +
                "    -fx-font-size: 16;\n" +
                "    -fx-border-color: #b8b8b8;\n" +
                "    -fx-border-radius: 8;\n" +
                "    -fx-background-radius: 8;");

        actionPane.getChildren().add(removeButton);
        itemPane.getChildren().add(name);
        quantityPane.getChildren().add(quantity);
        measurePane.getChildren().add(measure);

        actionPane.setPrefWidth(67);
        itemPane.setPrefWidth(200);
        quantityPane.setPrefWidth(89);
        measurePane.setPrefWidth(96);

        hbox.setPrefWidth(458);
        hbox.getChildren().addAll(actionPane, itemPane, quantityPane, measurePane);

        setGraphic(hbox);

        quantity.textProperty().addListener((obs, oldText, newText) -> {
            if (ingredient != null) {
                try {
                    double newQuantity = Double.parseDouble(newText);
                    controller.updateItemQuantity(ingredient, newQuantity);
                } catch (NumberFormatException e) {
                    controller.updateItemQuantity(ingredient, 0.0);
                }
            }
        });
    }

    @Override
    protected void updateItem(ProductIngredient ingredient, boolean empty) {
        super.updateItem(ingredient, empty);
        this.ingredient = ingredient;

        if (empty || ingredient == null) {
            setGraphic(null);
        } else {
            removeButton.setOnAction(e -> {
                controller.removeItemFromProduct(ingredient);
            });
            name.setText(ingredient.getItemName());
            measure.setText(ingredient.getUnitMeasure());
            quantity.setText(String.valueOf(ingredient.getNeededQuantity()));
            setGraphic(hbox);
        }
    }
}