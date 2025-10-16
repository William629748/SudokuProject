package com.example.demosudoku.model.game;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
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
                    board.lockCell(i, j);
                } else {
                    cell.setText("");
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
                    txt.setStyle("-fx-text-fill: red;");
                    return;
                }

                boolean valid = board.isValid(row, col, Integer.parseInt(input));
                txt.setStyle(valid ? "-fx-text-fill: #56b5c1;" : "-fx-text-fill: red;");
            }
        });
    }

    @Override
    public TextField getTextFieldAt(int row, int col) {
        for (Node node : boardGridpane.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if (r == null) r = 0;
            if (c == null) c = 0;
            if (r == row && c == col) return (TextField) node;
        }
        return null;
    }

    @Override
    public boolean isBoardComplete() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int num = board.getBoard().get(row).get(col);
                if (num == 0) return false;

                for (int c = 0; c < 6; c++) {
                    if (c != col && board.getBoard().get(row).get(c) == num) return false;
                }
                for (int r = 0; r < 6; r++) {
                    if (r != row && board.getBoard().get(r).get(col) == num) return false;
                }

                int startRow = (row / 2) * 2;
                int startCol = (col / 3) * 3;
                for (int r = startRow; r < startRow + 2; r++) {
                    for (int c = startCol; c < startCol + 3; c++) {
                        if ((r != row || c != col) && board.getBoard().get(r).get(c) == num)
                            return false;
                    }
                }
            }
        }
        return true;
    }

    public SuggestionEngine getSuggestionEngine() {
        return new SuggestionEngine();
    }

    public class SuggestionEngine {
        public int[] getSafeSuggestion() {
            for (int r = 0; r < 6; r++) {
                for (int c = 0; c < 6; c++) {
                    if (board.getBoard().get(r).get(c) == 0) {
                        List<Integer> options = getPossibleNumbers(r, c);
                        if (options.size() == 1)
                            return new int[]{r, c, options.get(0)};
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
            board.getBoard().get(r).set(c, num);
            TextField txt = getTextFieldAt(r, c);
            if (txt != null) txt.setText(String.valueOf(num));
            return true;
        }
    }
}
