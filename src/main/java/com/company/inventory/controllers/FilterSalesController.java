package com.company.inventory.controllers;

import com.company.inventory.models.Database;
import com.company.inventory.models.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class FilterSalesController {
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private ChoiceBox yearChoice;
    @FXML private ChoiceBox monthChoice;
    @FXML
    private ChoiceBox dayChoice;

    @FXML
    public void initialize() {

        yearChoice.setItems(Database.getDistinctYearsSales());

        yearChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                monthChoice.setItems(Database.getDistinctMonthsSales(newValue.toString()));
                monthChoice.setDisable(false);
            }
        });

        monthChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedYear = yearChoice.getSelectionModel().getSelectedItem().toString();
                dayChoice.setItems(Database.getDistinctDaysSales(selectedYear, newValue.toString()));
                dayChoice.setDisable(false);
            }
        });
    }

    @FXML
    public void confirmFilter(ActionEvent actionEvent) {
        String selectedYear = yearChoice.getSelectionModel().getSelectedItem() != null
                ? yearChoice.getSelectionModel().getSelectedItem().toString()
                : null;
        String selectedMonth = monthChoice.getSelectionModel().getSelectedItem() != null
                ? monthChoice.getSelectionModel().getSelectedItem().toString()
                : null;
        String selectedDay = dayChoice.getSelectionModel().getSelectedItem() != null
                ? dayChoice.getSelectionModel().getSelectedItem().toString()
                : null;

        // Check selections and filter accordingly
        ObservableList<Sale> filteredSales;
        try {
            if (selectedYear != null && selectedMonth == null && selectedDay == null) {
                // Filter by year only
                filteredSales = Database.filterSalesByYear(selectedYear);
            } else if (selectedYear != null && selectedMonth != null && selectedDay == null) {
                // Filter by year and month
                filteredSales = Database.filterSalesByYearAndMonth(selectedYear, selectedMonth);
            } else if (selectedYear != null && selectedMonth != null && selectedDay != null) {
                // Filter by year, month, and day
                filteredSales = Database.filterSalesByDate(selectedYear, selectedMonth, selectedDay);
            } else {
                showErrorDialog("Please select at least a year to filter sales.");
                return;
            }

            // Check if any sales are returned
            if (filteredSales.isEmpty()) {
                showErrorDialog("No sales found for the selected criteria.");
            } else {
                FXCollections.reverse(filteredSales);
                SalesController.getInstance().saleList.setItems(filteredSales);
                SalesController.getInstance().refreshSaleList();
            }
        } catch (Exception e) {
            showErrorDialog("An error occurred while filtering sales: " + e.getMessage());
        }

        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
