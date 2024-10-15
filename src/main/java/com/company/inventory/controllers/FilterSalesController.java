package com.company.inventory.controllers;

import com.company.inventory.models.Database;
import com.company.inventory.models.InventoryLog;
import com.company.inventory.models.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class FilterSalesController {
    @FXML private DatePicker datePickerFrom;
    @FXML private DatePicker datePickerTo;
    @FXML private DatePicker datePicker;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    @FXML
    public void confirmFilter(ActionEvent actionEvent) {
        LocalDate dateFrom = datePickerFrom.getValue();
        LocalDate dateTo = datePickerTo.getValue();

        // Ensure both date pickers have values
        if (dateFrom == null || dateTo == null) {
            showErrorDialog("Please select both 'From' and 'To' dates to filter sales.");
            return;
        }

        if (dateTo.isBefore(dateFrom)) {
            showErrorDialog("'To' date cannot be earlier than 'From' date.");
            return;
        }

        ObservableList<Sale> filteredSales;
        try {
            filteredSales = Database.filterSalesByDateRange(dateFrom, dateTo);

            if (filteredSales.isEmpty()) {
                showErrorDialog("No sales found for the selected date range.");
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
