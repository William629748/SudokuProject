package com.example.demosudoku.model.game;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;
import java.util.List;

public class Game extends GameAbstract {

    public Game(GridPane boardGridpane) {
        super(boardGridpane);
    }

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
                    cell.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold; -fx-background-color: #ecf0f1;");
                    board.lockCell(i, j);
                } else {
                    cell.setText("");
                    cell.setEditable(true);
                }

                handleNumberField(cell, i, j);
                boardGridpane.add(cell, j, i);
                numberFields.add(cell);
            }
        }
        board.unlockEmptyCells();
    }

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
                    txt.setStyle("-fx-text-fill: #27ae60; -fx-border-color: #27ae60; -fx-border-width: 1px;");
                } else {
                    // Número inválido - mostrar error
                    txt.setStyle("-fx-text-fill: #e74c3c; -fx-border-color: #e74c3c; -fx-border-width: 2px;");

                    // Mostrar alerta de conflicto
                    javafx.application.Platform.runLater(() -> {
                        // Puedes agregar aquí una alerta si lo deseas
                        System.out.println("Número " + number + " no válido en posición [" + row + "," + col + "]");
                    });
                }
            } else {
                // Celda vacía - resetear estilo y modelo
                board.getBoard().get(row).set(col, 0);
                txt.setStyle("");
            }
        });
    }

    @Override
    public TextField getTextFieldAt(int row, int col) {
        for (Node node : boardGridpane.getChildren()) {
            // SOLO procesar TextField, ignorar otros tipos de nodos
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

    @Override
    public boolean isBoardComplete() {
        try {
            // Primero verificar que no hay celdas vacías
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

            // Luego verificar que el tablero completo es válido
            boolean isValid = isValidCompleteBoard();
            System.out.println("Tablero completo. ¿Es válido? " + isValid);
            return isValid;

        } catch (Exception e) {
            System.err.println("Error en isBoardComplete: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Validar tablero completo
    private boolean isValidCompleteBoard() {
        try {
            for (int row = 0; row < 6; row++) {
                for (int col = 0; col < 6; col++) {
                    int num = board.getBoard().get(row).get(col);

                    // Validar fila
                    for (int c = 0; c < 6; c++) {
                        if (c != col && board.getBoard().get(row).get(c) == num) {
                            System.out.println("Error en fila " + row + ": número " + num + " repetido");
                            return false;
                        }
                    }

                    // Validar columna
                    for (int r = 0; r < 6; r++) {
                        if (r != row && board.getBoard().get(r).get(col) == num) {
                            System.out.println("Error en columna " + col + ": número " + num + " repetido");
                            return false;
                        }
                    }

                    // Validar bloque 2x3
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

    public SuggestionEngine getSuggestionEngine() {
        return new SuggestionEngine();
    }

    public class SuggestionEngine {
        public int[] getSafeSuggestion() {
            // Buscar celdas vacías y encontrar la solución correcta
            for (int r = 0; r < 6; r++) {
                for (int c = 0; c < 6; c++) {
                    if (board.getBoard().get(r).get(c) == 0) {
                        // Obtener sugerencia de la solución real
                        int suggestion = board.getSuggestion(r, c);
                        if (suggestion != 0) {
                            return new int[]{r, c, suggestion};
                        }
                    }
                }
            }
            return null;
        }

        private List<Integer> getPossibleNumbers(int row, int col) {
            List<Integer> list = new ArrayList<>();
            for (int n = 1; n <= 6; n++) {
                if (board.isValid(row, col, n)) list.add(n);
            }
            return list;
        }

        public boolean applySuggestionToBoard(int[] suggestion) {
            if (suggestion == null || suggestion.length < 3) return false;
            int r = suggestion[0];
            int c = suggestion[1];
            int num = suggestion[2];

            if (!board.isValid(r, c, num)) return false;

            // Actualizar modelo
            board.getBoard().get(r).set(c, num);

            // Actualizar vista
            TextField txt = getTextFieldAt(r, c);
            if (txt != null) {
                txt.setText(String.valueOf(num));
                txt.setStyle("-fx-text-fill: #2980b9; -fx-font-weight: bold; -fx-background-color: #d6eaf8;");
                txt.setEditable(false); // Hacer la celda no editable después de la ayuda
                board.lockCell(r, c); // Bloquear la celda
            }
            return true;
        }
    }
}