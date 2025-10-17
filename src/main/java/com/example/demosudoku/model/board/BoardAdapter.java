package com.example.demosudoku.model.board;

import java.util.List;

public class BoardAdapter implements IBoard {

    private final Board board;

    public BoardAdapter() {
        this.board = new Board();
    }

    @Override
    public boolean isValid(int row, int col, int candidate) {
        return board.isValid(row, col, candidate);
    }

    @Override
    public void regenerateBoard() {
        board.regenerateBoard();
    }

    @Override
    public void lockCell(int row, int col) {
        board.lockCell(row, col);
    }

    @Override
    public void unlockCell(int row, int col) {
        board.unlockCell(row, col);
    }

    @Override
    public boolean isCellLocked(int row, int col) {
        return board.isCellLocked(row, col);
    }

    @Override
    public void unlockEmptyCells() {
        board.unlockEmptyCells();
    }

    @Override
    public void cleanBoard() {
        board.cleanBoard();
    }

    @Override
    public List<List<Integer>> getBoard() {
        return board.getBoardAsList();
    }

    @Override
    public int getSuggestion(int row, int col) {
        return board.getSuggestion(row, col);
    }
}
