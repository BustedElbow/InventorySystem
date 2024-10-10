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
    @FXML private DatePicker datePicker;
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;

    @FXML
    public void initialize() {
        datePicker.setValue(LocalDate.now());
    }

    @FXML
    public void confirmFilter(ActionEvent actionEvent) {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            showErrorDialog("Please a select a date to filter inventory logs.");
            return;
        }

        ObservableList<InventoryLog> filteredLogs;
        try {

            String selectedYear = String.valueOf(selectedDate.getYear());
            String selectedMonth = String.format("%02d", selectedDate.getMonthValue());
            String selectedDay = String.format("%02d", selectedDate.getDayOfMonth());


            filteredLogs = Database.filterLogsByDate(selectedYear, selectedMonth, selectedDay);

            if (filteredLogs.isEmpty()) {
                showErrorDialog("No inventory logs found for the selected criteria.");
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
