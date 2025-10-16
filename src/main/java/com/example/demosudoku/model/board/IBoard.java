package com.example.demosudoku.model.board;

import java.util.List;

public interface IBoard {

    boolean isValid(int row, int col, int candidate);

    void regenerateBoard();

    void lockCell(int row, int col);

    void unlockCell(int row, int col);

    boolean isCellLocked(int row, int col);

    void unlockEmptyCells();

    void cleanBoard();

    List<List<Integer>> getBoard();

    int getSuggestion(int row, int col);
}
