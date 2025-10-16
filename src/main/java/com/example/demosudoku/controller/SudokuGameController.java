package com.example.demosudoku.controller;

import com.example.demosudoku.model.game.Game;
import com.example.demosudoku.model.user.User;
import com.example.demosudoku.utils.AlertBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SudokuGameController implements Initializable {

    @FXML
    private GridPane boardGridPane;

    private Game game;
    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new Game(boardGridPane);
        game.startGame();
        startVictoryCheck();
    }

    public void setUser(User user) {
        this.user = user;
    }

    @FXML
    private void handleReturnClick(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-welcome-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHelpButton(ActionEvent event) {
        System.out.println("Botón de ayuda presionado");

        if (game != null) {
            Game.SuggestionEngine suggestionEngine = game.getSuggestionEngine();
            int[] suggestion = suggestionEngine.getSafeSuggestion();

            if (suggestion != null) {
                boolean applied = suggestionEngine.applySuggestionToBoard(suggestion);
                if (applied) {
                    new AlertBox().showAlert("Ayuda",
                            "Se ha colocado el número " + suggestion[2] + " en la posición [" +
                                    (suggestion[0] + 1) + "," + (suggestion[1] + 1) + "]",
                            Alert.AlertType.INFORMATION);
                }
            } else {
                new AlertBox().showAlert("Ayuda",
                        "No se pudo encontrar una sugerencia en este momento",
                        Alert.AlertType.WARNING);
            }
        }
    }

    private void startVictoryCheck() {
        Thread victoryThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000); // Verificar cada segundo

                    if (game != null && game.isBoardComplete()) {
                        javafx.application.Platform.runLater(() -> {
                            try {
                                showVictoryScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        victoryThread.setDaemon(true);
        victoryThread.start();
    }

    private void showVictoryScreen() throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-final-view.fxml"));
            Parent root = loader.load();

            SudokuFinalController finalController = loader.getController();
            if (user != null) {
                finalController.setResult("¡Felicidades " + user.getNickname() + "! Has completado el Sudoku correctamente.");
            } else {
                finalController.setResult("¡Felicidades! Has completado el Sudoku correctamente.");
            }

            Stage currentStage = (Stage) boardGridPane.getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            new AlertBox().showAlert("Error", "No se pudo cargar la pantalla de victoria: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}