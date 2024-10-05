package com.company.inventory.controllers;

import com.company.inventory.models.Database;
import com.company.inventory.models.InventoryLog;
import com.company.inventory.models.Sale;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
        String selectedYear = yearChoice.getSelectionModel().getSelectedItem().toString();
        String selectedMonth = monthChoice.getSelectionModel().getSelectedItem().toString();
        String selectedDay = dayChoice.getSelectionModel().getSelectedItem().toString();

        ObservableList<Sale> filteredSales = Database.filterSalesByDate(selectedYear, selectedMonth, selectedDay);

        SalesController.getInstance().saleList.setItems(filteredSales);
        SalesController.getInstance().refreshSaleList();

        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }
    @FXML
    public void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

}
