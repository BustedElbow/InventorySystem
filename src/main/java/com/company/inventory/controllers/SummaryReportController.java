package com.company.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.util.List;

public class SummaryReportController {
    @FXML private ScrollPane pdfViewer;
    @FXML private VBox imageContainer;  // Add a VBox inside ScrollPane in FXML to hold images

    // Method to load images into the modal
    public void loadImages(List<String> imagePaths) {
        for (String path : imagePaths) {
            ImageView imageView = new ImageView(new Image("file:" + path));  // Load each image
            imageView.setFitWidth(800);  // Set width to fit the modal, adjust as needed
            imageView.setPreserveRatio(true);  // Keep the image aspect ratio
            imageContainer.getChildren().add(imageView);  // Add each image to the VBox container
        }
    }

    // Print the PDF report when the "Print" button is clicked
    public void printReport(ActionEvent actionEvent) {
        Printer printer = Printer.getDefaultPrinter();
        if (printer == null) {
            showAlert("No Printer Found", "Please install a printer.");
            return;
        }

        PrinterJob job = PrinterJob.createPrinterJob(printer);
        if (job != null && job.showPrintDialog(pdfViewer.getScene().getWindow())) {
            for (Node node : imageContainer.getChildren()) {
                if (node instanceof ImageView) {
                    boolean success = job.printPage(node);
                    if (!success) break;
                }
            }
            job.endJob();
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
