package com.company.inventory.controllers;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.factories.EditProdItemListCell;
import com.company.inventory.factories.EditProdUsedItemListCell;
import com.company.inventory.factories.ProdItemListCell;
import com.company.inventory.factories.ProdUsedItemListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Item;
import com.company.inventory.models.Product;
import com.company.inventory.models.ProductIngredient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EditProductController {
    @FXML
    private ListView itemList;
    @FXML private TextField productNameField;
    @FXML private TextField productPriceField;
    @FXML private ListView usedItemList;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button deleteBtn;
    private ObservableList<Item> items;
    private ObservableList<ProductIngredient> usedItems;
    private ObservableList<Item> copyItems;
    private Product product;
    private Map<ProductIngredient, Double> itemQuantities = new HashMap<>();

    @FXML
    public void initialize() {
        items = Database.getItemList();
        copyItems = FXCollections.observableArrayList(items);
        itemList.setItems(items);

        usedItems = FXCollections.observableArrayList();
        usedItemList.setItems(usedItems);

        itemList.getStylesheets().add(getClass().getResource("/styles/listview.css").toExternalForm());
        itemList.setCellFactory(param -> new EditProdItemListCell(this));

        usedItemList.getStylesheets().add(getClass().getResource("/styles/listview.css").toExternalForm());
        usedItemList.setCellFactory(param -> new EditProdUsedItemListCell(this));

    }

    public void setProduct(Product product) {
        this.product = product;
        productNameField.setText(product.getProductName());
        productPriceField.setText(String.valueOf(product.getProductPrice()));

        List<ProductIngredient> ingredients = product.getUsedItems();
        usedItemList.getItems().clear();
        usedItemList.getItems().addAll(ingredients);
    }

    public void saveBtn(ActionEvent actionEvent) {
        product.setProductName(productNameField.getText());
        product.setProductPrice(Double.parseDouble(productPriceField.getText()));

        product.update();

        saveUsedItemsToDatabase();

        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void btnDelete(ActionEvent actionEvent) {
        boolean confirmed = showConfirmationDialog("Are you sure you want to delete this product?");
        if (confirmed) {
            // Delete the product from the database
            deleteProductFromDatabase(product.getProductId());

            // Optionally, close the current window after deletion
            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();

            ProductController.getInstance().refreshProductList();
        }
    }

    public void addItemToProduct(Item item, double quantity) {
        if (!usedItems.contains(item)) {

            ProductIngredient productIngredient = new ProductIngredient(
                    item.getId(),
                    item.getName(),
                    quantity,
                    item.getUnitMeasure()
            );

            usedItems.add(productIngredient);
            itemQuantities.put(productIngredient, quantity);
            System.out.println(item.getName() + " added to product");
        }
    }

    public void removeItemFromProduct(ProductIngredient ingredient) {
        if(usedItems.contains(ingredient)) {
            usedItems.remove(ingredient);
            itemQuantities.remove(ingredient);

            deleteUsedItemFromDatabase(product.getProductId(), ingredient.getItemId());
        }
    }

    public void updateItemQuantity(ProductIngredient ingredient, double quantity) {
        itemQuantities.put(ingredient, quantity);
    }

    private void saveUsedItemsToDatabase() {
        for (ProductIngredient ingredient : usedItems) {
            double quantity = itemQuantities.get(ingredient);

            if (ingredientExistsInDatabase(ingredient)) {
                updateIngredientInDatabase(ingredient, quantity);
            } else {
                insertIngredientInDatabase(ingredient, quantity);
            }
        }
    }

    private boolean ingredientExistsInDatabase(ProductIngredient ingredient) {
        String query = "SELECT COUNT(*) FROM product_ingredients WHERE product_id = ? AND item_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, product.getProductId());
            pstmt.setInt(2, ingredient.getItemId());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void updateIngredientInDatabase(ProductIngredient ingredient, double quantity) {
        String query = "UPDATE product_ingredients SET needed_quantity = ? WHERE product_id = ? AND item_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, quantity);
            pstmt.setInt(2, product.getProductId());
            pstmt.setInt(3, ingredient.getItemId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertIngredientInDatabase(ProductIngredient ingredient, double quantity) {
        String query = "INSERT INTO product_ingredients (product_id, item_id, needed_quantity) VALUES (?, ?, ?)";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, product.getProductId());
            pstmt.setInt(2, ingredient.getItemId());
            pstmt.setDouble(3, quantity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void deleteUsedItemFromDatabase(int productId, int itemId) {
        String query = "DELETE FROM product_ingredients WHERE product_id = ? AND item_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, itemId);
            pstmt.executeUpdate();
            System.out.println("Removed ingredient with item ID " + itemId + " from product ID " + productId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteProductFromDatabase(int productId) {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate(); // Execute the delete
            System.out.println("Product with ID " + productId + " has been deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean showConfirmationDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Add buttons for "Yes" and "No"
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the dialog and wait for the user's response
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == yesButton; // Return true if "Yes" was clicked
    }
}
