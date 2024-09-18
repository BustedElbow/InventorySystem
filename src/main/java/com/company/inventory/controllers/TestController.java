package com.company.inventory.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import com.company.inventory.Db;

public class TestController {

    private Db db = new Db();

    @FXML
    private ListView<?> listView;

    @FXML
    private TextArea textArea;

    @FXML
    void buttonClear(MouseEvent event) {

    }

    @FXML
    void buttonDelete(MouseEvent event) {

    }

    @FXML
    void buttonSave(MouseEvent event) {
        db.insertNote("Testing123", textArea.getText());
    }

}
