package com.example.demosudoku.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the help/rules screen of the Sudoku game.
 * This controller manages navigation from the help screen back to the main menu.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class SudokuHelpController {

    /**
     * Handles the mouse click event on the menu button.
     * Navigates back to the welcome screen when the user clicks the menu icon.
     *
     * @param event the mouse event triggered by clicking the menu button
     */
    @FXML
    private void handleMenuClicked(MouseEvent event) {
        try {
            // Carga el FXML desde resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-welcome-view.fxml"));
            Parent root = loader.load();

            // Obtiene el stage actual desde la imagen clickeada
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}