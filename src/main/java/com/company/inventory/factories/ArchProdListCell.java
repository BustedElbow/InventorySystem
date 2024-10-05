package com.company.inventory.factories;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.controllers.EditProductController;
import com.company.inventory.controllers.ProductArchiveController;
import com.company.inventory.controllers.ProductController;
import com.company.inventory.models.Database;
import com.company.inventory.models.Product;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArchProdListCell extends ListCell<Product> {
    private HBox hbox = new HBox();
    private StackPane productPane = new StackPane();
    private StackPane pricePane = new StackPane();
    private Label productId = new Label();
    private Label productName = new Label();
    private Label productPrice = new Label();
    private Button restoreButton = new Button("<");


    public ArchProdListCell() {

        productPane.setAlignment(Pos.CENTER_LEFT);
        pricePane.setAlignment(Pos.CENTER_RIGHT);

        productId.setVisible(false);
        productId.setManaged(false);

        productPane.getChildren().addAll(productId, productName);
        pricePane.getChildren().addAll(productPrice, restoreButton);

        productPane.setPrefWidth(230);
        pricePane.setPrefWidth(230);

        hbox.setPadding(new Insets(4, 0 , 4, 0));
        hbox.setPrefWidth(518);
        hbox.getChildren().addAll(productPane, pricePane);

        restoreButton.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Restore");
            alert.setHeaderText("Are you sure you want to restore this product?");
            alert.setContentText("Product Name: " + productName.getText() + "\nPrice: " + productPrice.getText());

            // Show the alert and wait for the user's response
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // User confirmed, proceed with restoration
                    restoreProduct();
                    ProductController.getInstance().refreshProductList();
                    ProductArchiveController.getInstance().refreshArcProdList();
                }
            });
        });

        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if(empty || product == null) {
            setGraphic(null);
        } else {
            productName.setText(product.getProductName());
            productId.setText(Integer.toString(product.getProductId()));
            productPrice.setText(Double.toString(product.getProductPrice()));

            setGraphic(hbox);
        }
    }

    private void restoreProduct() {
        if (getItem() != null) {
            Product product = getItem();
            restoreProductToDatabase(product.getProductId());
        }
    }

    private void restoreProductToDatabase(int productId) {
        String restoreProductQuery = "INSERT INTO products (product_id, product_name, product_price) " +
                "SELECT product_id, product_name, product_price FROM archive_products " +
                "WHERE product_id = ?";

        String restoreIngredientsQuery = "INSERT INTO product_ingredients (product_id, item_id, needed_quantity) " +
                "SELECT product_id, item_id, needed_quantity FROM archive_prod_ingrd " +
                "WHERE product_id = ?";

        String deleteProductFromArchive = "DELETE FROM archive_products WHERE product_id = ?";
        String deleteIngredientsFromArchive = "DELETE FROM archive_prod_ingrd WHERE product_id = ?";

        try (Connection conn = SQLiteDatabase.connect()) {
            conn.setAutoCommit(false);

            try (PreparedStatement restoreProductStmt = conn.prepareStatement(restoreProductQuery)) {
                restoreProductStmt.setInt(1, productId);
                restoreProductStmt.executeUpdate();
            }

            try (PreparedStatement restoreIngredientsStmt = conn.prepareStatement(restoreIngredientsQuery)) {
                restoreIngredientsStmt.setInt(1, productId);
                restoreIngredientsStmt.executeUpdate();
            }

            try (PreparedStatement deleteProductStmt = conn.prepareStatement(deleteProductFromArchive)) {
                deleteProductStmt.setInt(1, productId);
                deleteProductStmt.executeUpdate();
            }

            try (PreparedStatement deleteIngredientsStmt = conn.prepareStatement(deleteIngredientsFromArchive)) {
                deleteIngredientsStmt.setInt(1, productId);
                deleteIngredientsStmt.executeUpdate();
            }

            conn.commit();
            System.out.println("Product with ID " + productId + " has been restored.");
        } catch (SQLException e) {
            System.out.println("Error restoring product: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
