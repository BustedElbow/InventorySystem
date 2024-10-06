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
import javafx.stage.Stage;

public class FilterLogController {
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private ChoiceBox yearChoice;
    @FXML private ChoiceBox monthChoice;
    @FXML
    private ChoiceBox dayChoice;

    @FXML
    public void initialize() {

        yearChoice.setItems(Database.getDistinctYearsLogs());

        yearChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                monthChoice.setItems(Database.getDistinctMonthsLogs(newValue.toString()));
                monthChoice.setDisable(false);
            }
        });

        monthChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String selectedYear = yearChoice.getSelectionModel().getSelectedItem().toString();
                dayChoice.setItems(Database.getDistinctDaysLogs(selectedYear, newValue.toString()));
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
        ObservableList<InventoryLog> filteredLogs;
        try {
            if (selectedYear != null && selectedMonth == null && selectedDay == null) {
                // Filter by year only
                filteredLogs = Database.filterLogsByYear(selectedYear);
            } else if (selectedYear != null && selectedMonth != null && selectedDay == null) {
                // Filter by year and month
                filteredLogs = Database.filterLogsByYearAndMonth(selectedYear, selectedMonth);
            } else if (selectedYear != null && selectedMonth != null && selectedDay != null) {
                // Filter by year, month, and day
                filteredLogs = Database.filterLogsByDate(selectedYear, selectedMonth, selectedDay);
            } else {
                showErrorDialog("Please select at least a year to filter sales.");
                return;
            }

            // Check if any sales are returned
            if (filteredLogs.isEmpty()) {
                showErrorDialog("No sales found for the selected criteria.");
            } else {
                FXCollections.reverse(filteredLogs);
                InventoryLogController.getInstance().logListView.setItems(filteredLogs);
                InventoryLogController.getInstance().refreshLogListView();
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
