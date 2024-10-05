package com.company.inventory.factories;

import com.company.inventory.controllers.EditItemController;
import com.company.inventory.models.Item;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryListCell extends ListCell<Item> {
    private HBox hbox = new HBox();
    private HBox actionBox = new HBox();
    private Label id = new Label();
    private Label name = new Label();
    private Label unit = new Label();
    private Label level = new Label();
    private Label stock = new Label();
    private Button editButton = new Button("Edit");
    private Button restockButton = new Button("Restock");
    private StackPane unitPane = new StackPane();
    private StackPane stockPane = new StackPane();
    private StackPane namePane = new StackPane();
    private StackPane actionPane = new StackPane();
    private StackPane levelPane = new StackPane();

    public InventoryListCell() {

        editButton.setStyle("-fx-background-radius: 8; -fx-background-color: #ee8850; -fx-text-fill: #1e1e1e; -fx-padding: 8 12 8 12; -fx-font-family: 'Inter Semi Bold'; -fx-font-size: 16; -fx-cursor: hand;");

        namePane.setAlignment(Pos.CENTER_LEFT);
        actionPane.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        id.setVisible(false);
        id.setManaged(false);

        actionBox.getChildren().addAll(restockButton, editButton);

        namePane.getChildren().addAll(id, name);
        stockPane.getChildren().add(stock);
        levelPane.getChildren().add(level);
        unitPane.getChildren().add(unit);
        actionPane.getChildren().addAll(actionBox);

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
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/editItem.fxml"));
                    Parent root = loader.load();


                    EditItemController editItemController = loader.getController();

                    editItemController.setItem(getItem());

                    Stage stage = new Stage();
                    stage.setTitle("Edit Item");
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();

                    getListView().refresh();

                } catch(IOException er) {
                    er.printStackTrace();
                }
            });
            restockButton.setOnAction(e -> {
                System.out.println("Restock for " + item.getName());
            });
            setGraphic(hbox);
        }
    }
}
