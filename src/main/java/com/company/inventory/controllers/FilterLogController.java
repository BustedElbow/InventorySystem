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

public class FilterLogController {
    @FXML private DatePicker datePickerTo;
    @FXML private DatePicker datePickerFrom;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    @FXML
    public void confirmFilter(ActionEvent actionEvent) {
        LocalDate dateFrom = datePickerFrom.getValue();
        LocalDate dateTo = datePickerTo.getValue();

        // Ensure both date pickers have values
        if (dateFrom == null || dateTo == null) {
            showErrorDialog("Please select both 'From' and 'To' dates to filter inventory logs.");
            return;
        }

        if (dateTo.isBefore(dateFrom)) {
            showErrorDialog("'To' date cannot be earlier than 'From' date.");
            return;
        }

        ObservableList<InventoryLog> filteredLogs;
        try {
            filteredLogs = Database.filterLogsByDateRange(dateFrom, dateTo);

            if (filteredLogs.isEmpty()) {
                showErrorDialog("No inventory logs found for the selected date range.");
            } else {
                FXCollections.reverse(filteredLogs);
                InventoryLogController.getInstance().logListView.setItems(filteredLogs);
                InventoryLogController.getInstance().refreshLogListView();
            }
        } catch (Exception e) {
            showErrorDialog("An error occurred while filtering inventory logs: " + e.getMessage());
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
