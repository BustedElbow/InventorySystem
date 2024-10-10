package com.company.inventory.controllers;

import com.company.inventory.factories.SaleDetailsListCell;
import com.company.inventory.factories.SaleListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.ProductSaleSummary;
import com.company.inventory.models.Sale;
import com.company.inventory.models.SaleDetails;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import com.itextpdf.io.font.PdfEncodings;

public class SalesController {

    private static SalesController instance;
    @FXML private Button salesReport;
    @FXML private Label orderIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label totalPriceLabel;
    @FXML private ListView<SaleDetails> saleListDetails;
    @FXML public ListView<Sale> saleList;

    public SalesController() {
        instance = this;
    }

    public void initialize() {
        refreshSalesItemList();

        saleList.setCellFactory(param -> new SaleListCell());

        saleListDetails.setCellFactory(param -> new SaleDetailsListCell());

        saleList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        saleList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displaySaleDetails(newSelection);
            }
        });

    }

    public void refreshSalesItemList() {
        saleList.getItems().clear();
        Database.reloadSalesFromDatabase();
        ObservableList<Sale> sales = Database.getSaleList();
        FXCollections.reverse(sales);
        saleList.setItems(sales);
    }

    public static SalesController getInstance() {
        return instance;
    }
    public void btnNewSale(ActionEvent event) {
        showModal();
    }

    public void showModal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newSale.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("New Sale");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displaySaleDetails(Sale sale) {
        orderIdLabel.setText(Integer.toString(sale.getSaleId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String formattedDate = sale.getSaleDate().format(formatter);
        dateLabel.setText(formattedDate);
        totalPriceLabel.setText(Double.toString(sale.getTotalAmount()));

        List<SaleDetails> productDetails = SaleDetails.loadOrderDetails(sale.getSaleId());
        ObservableList<SaleDetails> orderDetails = FXCollections.observableArrayList(productDetails);
        saleListDetails.setItems(orderDetails);
    }

    public void filterDateList(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/filterSales.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("Filter Date");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshSaleList() {
        saleList.refresh();
    }

    public void generateSalesReport(ActionEvent actionEvent) {
        LocalDate today = LocalDate.now(); // or a selected date
        generateSalesReportPDF(today);
    }
    public void generateSalesReportPDF(LocalDate date) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String dest = "SalesReport_" + date.toString() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Load a Unicode font that supports the peso sign
            String fontPath = getClass().getResource("/fonts/Inter-SemiBold.ttf").toExternalForm();
            PdfFont font = PdfFontFactory.createFont();

            // Title
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            document.add(new Paragraph("Sales Report for " + date.format(formatter)).setFont(font));

            // Get sales summary
            ObservableList<ProductSaleSummary> salesSummary = Database.getSalesSummaryForDay(date);

            // Create a table with four columns
            Table table = new Table(4);
            table.setWidth(UnitValue.createPercentValue(100));

            // Add headers
            table.addHeaderCell(new Cell().add(new Paragraph("Product Name").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Price").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Quantity").setFont(font)));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Price").setFont(font)));

            double grandTotal = 0.0;
            String pesoSign = "₱"; // Unicode peso sign

            for (ProductSaleSummary summary : salesSummary) {
                table.addCell(new Cell().add(new Paragraph(summary.getProductName()).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(pesoSign + " " + currencyFormat.format(summary.getProductPrice())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(Integer.toString(summary.getTotalQuantity())).setFont(font)));
                table.addCell(new Cell().add(new Paragraph(pesoSign + " " + currencyFormat.format(summary.getTotalPrice())).setFont(font)));

                grandTotal += summary.getTotalPrice();
            }

            // Add the table to the document
            document.add(table);

            // Add a grand total row
            document.add(new Paragraph("Total Sales: " + pesoSign + " " + currencyFormat.format(grandTotal)).setFont(font));

            document.close();
            System.out.println("PDF created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
