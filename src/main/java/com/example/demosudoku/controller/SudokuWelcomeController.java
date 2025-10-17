package com.example.demosudoku.controller;

import com.example.demosudoku.model.user.User;
import com.example.demosudoku.utils.AlertBox;
import com.example.demosudoku.view.SudokuGameStage;
import com.example.demosudoku.view.SudokuWelcomeStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the welcome screen (sudoku-welcome-view.fxml).
 * It handles user interaction for starting a new game, viewing rules,
 * and exiting the application.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class SudokuWelcomeController {

    /**
     * The TextField where the user enters their nickname.
     */
    @FXML
    private TextField nicknameTxt;

    /**
     * ImageView for the play button icon.
     */
    @FXML
    private ImageView playImage; // IMagen de jugar

    /**
     * ImageView for the exit button icon.
     */
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

    /**
     * Handles the click event on the rules button.
     * Navigates to the help/rules screen to display game instructions.
     *
     * @param event the mouse event triggered by clicking the rules button
     */
    @FXML
    private void handleRulesClicked(MouseEvent event) {
        try {
            // Carga el FXML desde resources
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-help-view.fxml"));
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

    /**
     * Handles the click event on the exit button.
     * Terminates the application when the user clicks the exit icon.
     *
     * @param event the mouse event triggered by clicking the exit button
     */
    @FXML
    private void handleExitImageClick(MouseEvent event) {
        System.out.println("Cerrar aplicación");
        System.exit(0);
    }

}