package com.example.demosudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Singleton class representing the final screen (victory screen).
 * This stage is displayed when the player successfully completes the Sudoku puzzle.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class SudokuFinalStage extends Stage {

    /**
     * The singleton instance of SudokuFinalStage.
     */
    private static SudokuFinalStage instance;

    /**
     * Private constructor to enforce singleton pattern.
     * Loads the FXML view and sets up the stage.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    private SudokuFinalStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-final-view.fxml"));
        Scene scene = new Scene(loader.load());
        setTitle("Sudoku - Final");
        setScene(scene);
        show();
    }

    /**
     * Gets the singleton instance of SudokuFinalStage.
     * Creates the instance if it doesn't exist yet.
     *
     * @return the singleton instance
     * @throws IOException if the FXML file cannot be loaded during the first creation
     */
    public static SudokuFinalStage getInstance() throws IOException {
        if (instance == null) {
            instance = new SudokuFinalStage();
        }
        return instance;
    }

    /**
     * Closes and deletes the singleton instance.
     */
    public static void deleteInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }

    /**
     * Sets the result message to be displayed on the final screen.
     * This method is currently a placeholder and should be implemented
     * to actually update the UI.
     *
     * @param s the result message to display
     */
    public void setResult(String s) {
    }
}