package com.example.demosudoku.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the final victory screen of the Sudoku game.
 * This controller manages the display of the completion message and handles
 * the exit functionality when the user clicks on the door icon.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class SudokuFinalController {

    /**
     * Label displaying the victory message to the user.
     * The fx:id in the FXML file must match this variable name.
     */
    @FXML
    private Label resultLabel; // Asegúrate de que este nombre coincida con el fx:id en el FXML

    /**
     * Sets the result message to be displayed on the final screen.
     * This method is called from the game controller when the player completes
     * the Sudoku puzzle successfully.
     *
     * @param message the victory message to display, typically includes the player's nickname
     */
    public void setResult(String message) {
        if (resultLabel != null) {
            resultLabel.setText(message);
        } else {
            System.err.println("Error: resultLabel es null en SudokuFinalController");
        }
    }

    /**
     * Handles the mouse click event on the door icon.
     * When the user clicks the door, the application exits completely.
     *
     * @param event the mouse event triggered by clicking the door icon
     */
    @FXML
    private void handleDoorClicked(MouseEvent event) {
        System.out.println("Cerrar aplicación");
        System.exit(0);
    }
}