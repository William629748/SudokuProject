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
    private Label resultLabel;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setResult(String message) {
        resultLabel.setText(message);
    }

    @FXML
    private void onReturnToMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/view/sudoku-menu-view.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
