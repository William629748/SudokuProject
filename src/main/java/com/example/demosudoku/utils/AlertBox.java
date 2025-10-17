package com.example.demosudoku.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * A utility class for creating and displaying standard JavaFX alerts.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class AlertBox implements IAlertBox {

    /**
     * The Alert instance used to display messages.
     */
    private Alert alert;

    /**
     * Displays an alert dialog to the user.
     *
     * @param headerText The text to display in the header area of the dialog.
     * @param contentText The main content message of the dialog.
     * @param alertType The type of alert to display (e.g., ERROR, INFORMATION).
     */
    @Override
    public void showAlert(String headerText, String contentText, AlertType alertType) {
        alert = new Alert(alertType);
        alert.setTitle("Sudoku");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}