package com.company.inventory.controllers;

import com.company.inventory.models.*;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReportFilterController {
    @FXML private Button confirmBtn;
    @FXML private Button cancelBtn;
    @FXML private DatePicker datePickerFrom;
    @FXML private DatePicker datePickerTo;

    public void confirmFilter(ActionEvent actionEvent) {
        LocalDate fromDate = datePickerFrom.getValue();
        LocalDate toDate = datePickerTo.getValue();

        if (fromDate == null || toDate == null || fromDate.isAfter(toDate)) {
            showAlert("Invalid Date Range", "Please select a valid date range.");
            return;
        }

        generatePDFReport(fromDate, toDate);

        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }


    public void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void generatePDFReport(LocalDate fromDate, LocalDate toDate) {
        ObservableList<ProductSaleSummary> salesSummary = Database.getSalesDetailsForDateRange(fromDate, toDate);
        if (salesSummary.isEmpty()) {
            showAlert("No Sales Found", "No sales records found for the selected date range.");
            return; // Early exit if no sales
        }
        ObservableList<InventoryUsageSummary> inventorySummary = Database.getInventoryUsageForDateRange(fromDate, toDate);


        if (inventorySummary.isEmpty()) {
            showAlert("No Inventory Found", "No inventory records found for the selected date range.");
            return; // Early exit if no inventory
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault());
        String dest = "SalesInventoryReport_" + fromDate.toString() + "_to_" + toDate.toString() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(dest);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            String fontPath = getClass().getResource("/fonts/Inter-SemiBold.ttf").toExternalForm();
            PdfFont font = PdfFontFactory.createFont(fontPath, PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            document.add(new Paragraph("Sales and Inventory Report from " + fromDate.format(formatter) + " to " + toDate.format(formatter))
                    .setFont(font));

            // Sales Section
            document.add(new Paragraph("Sales Summary").setFont(font));

            Table salesTable = new Table(4);
            salesTable.setWidth(UnitValue.createPercentValue(100));
            salesTable.addHeaderCell(new Cell().add(new Paragraph("Product Name").setFont(font)));
            salesTable.addHeaderCell(new Cell().add(new Paragraph("Price").setFont(font)));
            salesTable.addHeaderCell(new Cell().add(new Paragraph("Total Quantity Sold").setFont(font)));
            salesTable.addHeaderCell(new Cell().add(new Paragraph("Total Amount").setFont(font)));

            double grandTotalSales = 0.0;

            for (ProductSaleSummary summary : salesSummary) {
                salesTable.addCell(new Cell().add(new Paragraph(summary.getProductName()).setFont(font)));
                salesTable.addCell(new Cell().add(new Paragraph(currencyFormat.format(summary.getProductPrice())).setFont(font)));

                salesTable.addCell(new Cell().add(new Paragraph(Integer.toString(summary.getTotalQuantity())).setFont(font)));
                salesTable.addCell(new Cell().add(new Paragraph(currencyFormat.format(summary.getTotalPrice())).setFont(font)));

                grandTotalSales += summary.getTotalPrice();
            }

            document.add(salesTable);
            document.add(new Paragraph("Total Sales: " + currencyFormat.format(grandTotalSales)).setFont(font));

            // Fetch inventory log summary

            document.add(new Paragraph("Inventory Usage Summary").setFont(font));

            Table inventoryTable = new Table(3);
            inventoryTable.setWidth(UnitValue.createPercentValue(100));
            inventoryTable.addHeaderCell(new Cell().add(new Paragraph("Item Name").setFont(font)));
            inventoryTable.addHeaderCell(new Cell().add(new Paragraph("Total Quantity Used").setFont(font)));
            inventoryTable.addHeaderCell(new Cell().add(new Paragraph("Current Stock").setFont(font)));

            for (InventoryUsageSummary usageSummary : inventorySummary) {
                inventoryTable.addCell(new Cell().add(new Paragraph(usageSummary.getItemName()).setFont(font)));
                // Use Math.abs() to remove negative sign
                inventoryTable.addCell(new Cell().add(new Paragraph(Math.abs(usageSummary.getTotalQuantityUsed()) + " " + usageSummary.getUnitMeasure()).setFont(font))); // Include unit measure
                inventoryTable.addCell(new Cell().add(new Paragraph(Math.abs(usageSummary.getCurrentStock()) + " " + usageSummary.getUnitMeasure()).setFont(font))); // Use Math.abs() here too if needed
            }

            document.add(inventoryTable);
            document.close();
            System.out.println("PDF created successfully!");

            List<String> imagePaths = convertPDFToImages(dest);  // Convert PDF to images and get file paths
            openImageModal(imagePaths);

        } catch (IOException e) {
            showAlert("Error", "An error occurred while creating the PDF: " + e.getMessage());
            e.printStackTrace();  // Log stack trace for debugging
        } catch (Exception e) {
            showAlert("Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();  // Log stack trace for debugging
        }
    }

    private List<String> convertPDFToImages(String pdfFilePath) {
        List<String> imagePaths = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
                String imageFileName = "SalesInventoryReport_Page_" + (page + 1) + ".png";
                ImageIO.write(image, "png", new File(imageFileName));
                imagePaths.add(imageFileName);
                System.out.println("Saved: " + imageFileName);
            }
        } catch (IOException e) {
            showAlert("Error", "An error occurred while converting the PDF to images: " + e.getMessage());
            e.printStackTrace();
        }
        return imagePaths;  // Return list of saved image paths
    }

    private void openImageModal(List<String> imagePaths) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/summaryReport.fxml"));
            Parent root = loader.load();

            SummaryReportController controller = loader.getController();
            controller.loadImages(imagePaths);  // Pass the image paths to load in the modal

            Stage stage = new Stage();
            stage.setTitle("Sales and Inventory Report");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Error", "Failed to open the image modal.");
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}