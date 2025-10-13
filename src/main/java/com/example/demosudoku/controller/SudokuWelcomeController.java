package com.example.demosudoku.controller;

import com.example.demosudoku.model.user.User;
import com.example.demosudoku.utils.AlertBox;
import com.example.demosudoku.view.SudokuGameStage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Controller for the welcome screen (sudoku-welcome-view.fxml).
 * It handles user interaction for starting a new game.
 */
public class SudokuWelcomeController {

    /**
     * The TextField where the user enters their nickname.
     */
    @FXML
    private TextField nicknameTxt;

    @FXML
    private ImageView playImage; // IMagen de jugar

    @FXML
    private ImageView exitImage; //IMagen de sair

    /**
     * Handles the "Play" button action. It validates the user's nickname,
     * creates a new User, passes it to the game controller, and transitions
     * from the welcome stage to the game stage.
     *
     * @param event The action event triggered by clicking the button.
     * @throws IOException If the SudokuGameStage FXML file cannot be loaded.
     */
    @FXML
    void handlePlayImageClick(MouseEvent event) throws IOException {
        String nickname = nicknameTxt.getText().trim();

        if (!nickname.isEmpty()) {
            SudokuGameStage.getInstance().getController().setUser(new User(nickname));
            SudokuWelcomeStage.deleteInstance();
        } else {
            new AlertBox().showAlert("Error", "Ingresa un nickname", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleExitImageClick(MouseEvent event) {
        System.out.println("Cerrar aplicación");
        System.exit(0);
    }

}
