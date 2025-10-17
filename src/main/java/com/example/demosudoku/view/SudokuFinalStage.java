package com.example.demosudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Singleton class representing the final screen (SudokuFinalStage).
 * Opens when the player completes the Sudoku.
 */
public class SudokuFinalStage extends Stage {

    private static SudokuFinalStage instance;

    private SudokuFinalStage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-final-view.fxml"));
        Scene scene = new Scene(loader.load());
        setTitle("Sudoku - Final");
        setScene(scene);
        show();
    }

    public static SudokuFinalStage getInstance() throws IOException {
        if (instance == null) {
            instance = new SudokuFinalStage();
        }
        return instance;
    }

    public static void deleteInstance() {
        if (instance != null) {
            instance.close();
            instance = null;
        }
    }

    public void setResult(String s) {
    }
}
