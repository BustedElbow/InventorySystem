package com.company.inventory.controllers;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.factories.ArchProdListCell;
import com.company.inventory.factories.ProdDetailsListCell;
import com.company.inventory.factories.ProductListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Product;
import com.company.inventory.models.ProductIngredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ProductArchiveController {
    private static ProductArchiveController instance;
    @FXML private Label archiveDateTime;
    @FXML private ListView<Product> archProdListView;
    @FXML private ListView<ProductIngredient> archProdDetailsList;
    @FXML private Label productDetailsName;
    @FXML private Label productDetailsPrice;
    @FXML private Button productsBtn;

    public ProductArchiveController() {
        instance = this;
    }
    @FXML
    public void initialize() {
        refreshArcProdList();

        archProdListView.setCellFactory(param -> new ArchProdListCell());


        archProdListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayArchivedProductDetails(newSelection);
            }
        });

        archProdDetailsList.setCellFactory(param -> new ProdDetailsListCell());
    }

    private void displayArchivedProductDetails(Product product) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());

        archProdDetailsList.getItems().clear();

        productDetailsName.setText(product.getProductName());
        productDetailsPrice.setText(currencyFormat.format(product.getProductPrice()));

        LocalDateTime archiveDateTime = fetchArchiveDateTime(product.getProductId());
        if (archiveDateTime != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            String formattedDateTime = archiveDateTime.format(formatter);
            this.archiveDateTime.setText(formattedDateTime);
        } else {
            this.archiveDateTime.setText("N/A");
        }

        List<ProductIngredient> ingredients = product.getArcUsedItems();
        ObservableList<ProductIngredient> usedItemObs = FXCollections.observableArrayList(ingredients);

        archProdDetailsList.setItems(usedItemObs);
    }

    public void refreshArcProdList() {
        Database.reloadArchProdFromDatabase();
        ObservableList<Product> products = Database.getArchivedProductList();
        archProdListView.setItems(products);
    }

    public static ProductArchiveController getInstance() {
        return instance;
    }

    public void showProducts(ActionEvent actionEvent) {
        LayoutController.getInstance().loadProducts();
    }

    private LocalDateTime fetchArchiveDateTime(int productId) {
        LocalDateTime archiveDateTime = null;
        String query = "SELECT archive_date FROM archive_products WHERE product_id = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                archiveDateTime = LocalDateTime.parse(rs.getString("archive_date"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return archiveDateTime;
    }
}
