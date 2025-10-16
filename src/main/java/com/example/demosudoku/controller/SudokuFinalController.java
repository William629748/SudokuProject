package com.example.demosudoku.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class SudokuFinalController {

    @FXML
    private Label resultLabel; // Asegúrate de que este nombre coincida con el fx:id en el FXML

    public void setResult(String message) {
        if (resultLabel != null) {
            resultLabel.setText(message);
        } else {
            System.err.println("Error: resultLabel es null en SudokuFinalController");
        }
    }

    @FXML
    private void handleMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-welcome-view.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) resultLabel.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}