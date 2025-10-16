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
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(2000); // Verificar cada 2 segundos (más eficiente)

                    // Verificar si el juego existe y el tablero está completo
                    if (game != null && game.isBoardComplete()) {
                        System.out.println("¡Sudoku completado! Mostrando pantalla de victoria...");

                        javafx.application.Platform.runLater(() -> {
                            try {
                                showVictoryScreen();
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.err.println("Error al mostrar pantalla de victoria: " + e.getMessage());
                            }
                        });

                        // Salir del bucle después de detectar victoria
                        break;
                    }
                } catch (InterruptedException e) {
                    System.out.println("Hilo de verificación interrumpido");
                    break;
                } catch (Exception e) {
                    System.err.println("Error en el hilo de verificación: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        victoryThread.setDaemon(true);
        victoryThread.setName("Victory-Check-Thread");
        victoryThread.start();

        // Guardar referencia para poder detenerlo si es necesario
        this.victoryThread = victoryThread;
    }

    // Variable de instancia para el hilo
    private Thread victoryThread;

    // Método para mostrar la pantalla de victoria
    private void showVictoryScreen() throws IOException {
        try {
            // Detener el hilo de verificación
            if (victoryThread != null && victoryThread.isAlive()) {
                victoryThread.interrupt();
            }

            // Cargar la pantalla final
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-final-view.fxml"));
            Parent root = loader.load();

            // Configurar el mensaje de victoria
            SudokuFinalController finalController = loader.getController();

            // Verificar que el controlador se cargó correctamente
            if (finalController == null) {
                throw new IOException("No se pudo cargar el controlador de la pantalla final");
            }

            String victoryMessage;
            if (user != null) {
                victoryMessage = "¡Felicidades " + user.getNickname() + "! Has completado el Sudoku correctamente.";
            } else {
                victoryMessage = "¡Felicidades! Has completado el Sudoku correctamente.";
            }

            finalController.setResult(victoryMessage);

            // Cambiar a la pantalla final
            Stage currentStage = (Stage) boardGridPane.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new AlertBox().showAlert("Error", "No se pudo cargar la pantalla de victoria: " + e.getMessage(), Alert.AlertType.ERROR);

            // Fallback: mostrar mensaje de victoria en una alerta
            String victoryMessage = user != null ?
                    "¡Felicidades " + user.getNickname() + "! Has completado el Sudoku correctamente." :
                    "¡Felicidades! Has completado el Sudoku correctamente.";

            new AlertBox().showAlert("¡Victoria!", victoryMessage, Alert.AlertType.INFORMATION);
        }
    }
}