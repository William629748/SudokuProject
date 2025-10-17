package com.example.demosudoku.model.game;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

/**
 * Main game logic class for the Sudoku game.
 * Handles the game board UI, user input validation, and game state management.
 * This class extends GameAbstract and provides concrete implementations
 * for starting the game, validating moves, and checking for completion.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class Game extends GameAbstract {

    /**
     * Constructs a new Game instance with the specified GridPane.
     *
     * @param boardGridpane the GridPane that will contain the Sudoku board cells
     */
    public Game(GridPane boardGridpane) {
        super(boardGridpane);
    }

    /**
     * Initializes and starts the game by populating the board with TextFields.
     * Sets up initial values from the board model, applies styling, and
     * configures event handlers for user input.
     */
    @Override
    public void startGame() {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                int number = board.getBoard().get(i).get(j);
                TextField cell = new TextField();
                cell.setBackground(Background.EMPTY);
                cell.setAlignment(Pos.CENTER);

                if (number != 0) {
                    cell.setText(String.valueOf(number));
                    cell.setEditable(false);
                    // Fondo transparente en lugar de blanco
                    cell.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-background-color: transparent;");
                    board.lockCell(i, j);
                } else {
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setStyle("-fx-background-color: transparent;"); // Fondo transparente también en celdas vacías
                }

                handleNumberField(cell, i, j);
                boardGridpane.add(cell, j, i);
                numberFields.add(cell);
            }
        }
        board.unlockEmptyCells();
    }

    /**
     * Configures the event handler for a TextField cell.
     * Validates user input, updates the board model, and applies appropriate styling
     * based on whether the input is valid or invalid.
     *
     * @param txt the TextField to configure
     * @param row the row index of the cell
     * @param col the column index of the cell
     */
    private void handleNumberField(TextField txt, int row, int col) {
        txt.setOnKeyReleased(event -> {
            String input = txt.getText().trim();

            if (!input.isEmpty()) {
                if (!input.matches("[1-6]")) {
                    txt.setText("");
                    txt.setStyle("-fx-text-fill: red; -fx-border-color: red; -fx-border-width: 2px;");
                    return;
                }

                int number = Integer.parseInt(input);
                boolean valid = board.isValid(row, col, number);

                if (valid) {
                    // Actualizar el modelo con el número válido
                    board.getBoard().get(row).set(col, number);
                    txt.setStyle("-fx-text-fill: #27ae60; -fx-border-color: #27ae60; -fx-border-width: 1px; -fx-background-color: transparent;");
                } else {
                    // Número inválido - mostrar error
                    txt.setStyle("-fx-text-fill: #e74c3c; -fx-border-color: #e74c3c; -fx-border-width: 2px; -fx-background-color: transparent;");

                    // Mostrar alerta de conflicto
                    javafx.application.Platform.runLater(() -> {
                        System.out.println("Número " + number + " no válido en posición [" + row + "," + col + "]");
                    });
                }
            } else {
                // Celda vacía - resetear estilo y modelo
                board.getBoard().get(row).set(col, 0);
                txt.setStyle("-fx-background-color: transparent;");
            }
        });
    }

    /**
     * Retrieves the TextField at a specific row and column position.
     *
     * @param row the row index
     * @param col the column index
     * @return the TextField at the specified position, or null if not found
     */
    @Override
    public TextField getTextFieldAt(int row, int col) {
        for (Node node : boardGridpane.getChildren()) {
            if (node instanceof TextField) {
                Integer r = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);
                if (r == null) r = 0;
                if (c == null) c = 0;
                if (r == row && c == col) return (TextField) node;
            }
        }
        return null;
    }

    /**
     * Checks if the board is completely filled and valid.
     * Verifies that all cells are filled and that the board follows Sudoku rules.
     *
     * @return true if the board is complete and valid, false otherwise
     */
    @Override
    public boolean isBoardComplete() {
        try {
            boolean hasEmptyCells = false;
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    if (board.getBoard().get(row).get(col) == 0) {
                        hasEmptyCells = true;
                        break;
                    }
                }
                if (hasEmptyCells) break;
            }

            if (hasEmptyCells) {
                System.out.println("Tablero incompleto: hay celdas vacías");
                return false;
            }

            boolean isValid = isValidCompleteBoard();
            System.out.println("Tablero completo. ¿Es válido? " + isValid);
            return isValid;

        } catch (Exception e) {
            System.err.println("Error en isBoardComplete: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates the complete board by checking all Sudoku constraints.
     * Verifies that no number is repeated in any row, column, or 2x3 block.
     *
     * @return true if the completed board is valid, false otherwise
     */
    private boolean isValidCompleteBoard() {
        try {
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    int num = board.getBoard().get(row).get(col);

                    for (int c = 0; c < 6; c++) {
                        if (c != col && board.getBoard().get(row).get(c) == num) {
                            System.out.println("Error en fila " + row + ": número " + num + " repetido");
                            return false;
                        }
                    }

                    for (int r = 0; r < 6; r++) {
                        if (r != row && board.getBoard().get(r).get(col) == num) {
                            System.out.println("Error en columna " + col + ": número " + num + " repetido");
                            return false;
                        }
                    }

                    int startRow = (row / 2) * 2;
                    int startCol = (col / 3) * 3;
                    for (int r = startRow; r < startRow + 2; r++) {
                        for (int c = startCol; c < startCol + 3; c++) {
                            if ((r != row || c != col) && board.getBoard().get(r).get(c) == num) {
                                System.out.println("Error en bloque [" + startRow + "," + startCol + "]: número " + num + " repetido");
                                return false;
                            }
                        }
                    }
                }
            }
            System.out.println("¡Tablero válido y completo!");
            return true;

        } catch (Exception e) {
            System.err.println("Error en isValidCompleteBoard: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gets the SuggestionEngine for this game instance.
     *
     * @return a new SuggestionEngine instance
     */
    public SuggestionEngine getSuggestionEngine() {
        return new SuggestionEngine();
    }

    /**
     * Inner class that provides hint/suggestion functionality for the game.
     * Can find safe suggestions and apply them to the board.
     */
    public class SuggestionEngine {

        /**
         * Finds a safe suggestion for the next move.
         * Searches for an empty cell and returns a valid number that can be placed there.
         *
         * @return an array containing [row, column, suggested number], or null if no suggestion is available
         */
        public int[] getSafeSuggestion() {
            for (int r = 0; r < 6; r++) {
                for (int c = 0; c < 6; c++) {
                    if (board.getBoard().get(r).get(c) == 0) {
                        int suggestion = board.getSuggestion(r, c);
                        if (suggestion != 0) {
                            return new int[]{r, c, suggestion};
                        }
                    }
                }
            }
            return null;
        }

        /**
         * Gets all possible valid numbers for a specific cell.
         *
         * @param row the row index
         * @param col the column index
         * @return a list of valid numbers that can be placed at this position
         */
        private List<Integer> getPossibleNumbers(int row, int col) {
            List<Integer> list = new ArrayList<>();
            for (int n = 1; n <= 6; n++) {
                if (board.isValid(row, col, n)) list.add(n);
            }
            return list;
        }

        /**
         * Applies a suggestion to the board by filling in the suggested cell.
         * Updates both the model and the UI, locks the cell, and applies styling.
         *
         * @param suggestion an array containing [row, column, number to place]
         * @return true if the suggestion was successfully applied, false otherwise
         */
        public boolean applySuggestionToBoard(int[] suggestion) {
            if (suggestion == null || suggestion.length < 3) return false;
            int r = suggestion[0];
            int c = suggestion[1];
            int num = suggestion[2];

            if (!board.isValid(r, c, num)) return false;

            board.getBoard().get(r).set(c, num);

            TextField txt = getTextFieldAt(r, c);
            if (txt != null) {
                txt.setText(String.valueOf(num));
                txt.setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold; -fx-background-color: transparent;");
                txt.setEditable(false);
                board.lockCell(r, c);
            }
            return true;
        }
    }
}