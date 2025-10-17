package com.example.demosudoku.controller;

import com.example.demosudoku.model.game.Game;
import com.example.demosudoku.model.user.User;
import com.example.demosudoku.utils.AlertBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the main Sudoku game screen.
 * This controller manages the game board, user interactions during gameplay,
 * hint functionality, and victory detection.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class SudokuGameController implements Initializable {

    /**
     * The GridPane that contains the Sudoku board cells.
     */
    @FXML
    private GridPane boardGridPane;

    /**
     * ImageView for the hint/help button icon.
     */
    @FXML
    private ImageView pista;

    /**
     * The Game instance that manages the Sudoku logic and board state.
     */
    private Game game;

    /**
     * The current user playing the game.
     */
    private User user;

    /**
     * Background thread that periodically checks if the board is complete and valid.
     */
    private Thread victoryThread;

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the game, starts the board, configures the hint button, and starts victory checking.
     *
     * @param url the location used to resolve relative paths for the root object, or null if not known
     * @param resourceBundle the resources used to localize the root object, or null if not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game = new Game(boardGridPane);
        game.startGame();
        startVictoryCheck();

        // Configurar click sobre la imagen de ayuda
        pista.setOnMouseClicked(this::handleHelpButton);
    }

    /**
     * Sets the user for the current game session.
     *
     * @param user the User object containing the player's information
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Handles the return button click event.
     * Navigates back to the welcome screen when the user clicks the return button.
     *
     * @param event the mouse event triggered by clicking the return button
     */
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

    /**
     * Handles the help/hint button click event.
     * Requests a suggestion from the game's suggestion engine and applies it to the board.
     * If a valid suggestion is found, it is applied and the cell is highlighted.
     * If no suggestion is available, a warning alert is displayed.
     *
     * @param event the mouse event triggered by clicking the help button
     */
    @FXML
    private void handleHelpButton(MouseEvent event) {
        if (game != null) {
            Game.SuggestionEngine suggestionEngine = game.getSuggestionEngine();
            int[] suggestion = suggestionEngine.getSafeSuggestion();

            if (suggestion != null) {
                // Aplica la sugerencia sin mostrar alerta
                suggestionEngine.applySuggestionToBoard(suggestion);
                highlightSuggestionCell(suggestion[0], suggestion[1]);
            } else {
                // Solo mostrar alerta si no hay sugerencia disponible
                new AlertBox().showAlert("Ayuda",
                        "No se pudo encontrar una sugerencia en este momento",
                        Alert.AlertType.WARNING);
            }
        }
    }

    /**
     * Highlights a cell on the board temporarily after applying a suggestion.
     * The cell is given a light green background for 500 milliseconds.
     *
     * @param row the row index of the cell to highlight
     * @param col the column index of the cell to highlight
     */
    private void highlightSuggestionCell(int row, int col) {
        Node cell = getNodeFromGridPane(boardGridPane, col, row);
        if (cell != null) {
            String originalStyle = cell.getStyle();
            cell.setStyle("-fx-background-color: lightgreen; -fx-border-color: black;");

            // Restaurar estilo original después de 500 ms
            new Thread(() -> {
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                javafx.application.Platform.runLater(() -> cell.setStyle(originalStyle));
            }).start();
        }
    }

    /**
     * Retrieves a specific node from the GridPane based on row and column indices.
     *
     * @param grid the GridPane to search
     * @param col the column index of the desired node
     * @param row the row index of the desired node
     * @return the Node at the specified position, or null if not found
     */
    private Node getNodeFromGridPane(GridPane grid, int col, int row) {
        for (Node node : grid.getChildren()) {
            Integer nodeCol = GridPane.getColumnIndex(node);
            Integer nodeRow = GridPane.getRowIndex(node);
            if (nodeCol != null && nodeRow != null && nodeCol == col && nodeRow == row) {
                return node;
            }
        }
        return null;
    }

    /**
     * Starts a background thread that periodically checks if the board is complete and valid.
     * When the board is successfully completed, the victory screen is displayed.
     * The thread checks every 2 seconds and runs as a daemon thread.
     */
    private void startVictoryCheck() {
        Thread victoryThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(2000); // Verificar cada 2 segundos

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

        this.victoryThread = victoryThread;
    }

    /**
     * Displays the victory screen when the player completes the Sudoku puzzle.
     * Interrupts the victory checking thread and loads the final screen with a
     * personalized victory message for the user.
     *
     * @throws IOException if the victory screen FXML file cannot be loaded
     */
    private void showVictoryScreen() throws IOException {
        try {
            if (victoryThread != null && victoryThread.isAlive()) {
                victoryThread.interrupt();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demosudoku/sudoku-final-view.fxml"));
            Parent root = loader.load();

            SudokuFinalController finalController = loader.getController();
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

            Stage currentStage = (Stage) boardGridPane.getScene().getWindow();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            new AlertBox().showAlert("Error", "No se pudo cargar la pantalla de victoria: " + e.getMessage(), Alert.AlertType.ERROR);

            String victoryMessage = user != null ?
                    "¡Felicidades " + user.getNickname() + "! Has completado el Sudoku correctamente." :
                    "¡Felicidades! Has completado el Sudoku correctamente.";

            new AlertBox().showAlert("¡Victoria!", victoryMessage, Alert.AlertType.INFORMATION);
        }
    }
}