package com.example.demosudoku.model.board;

import java.util.List;

/**
 * Interface defining the contract for Sudoku board operations.
 * Provides methods for board validation, manipulation, and state management.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public interface IBoard {

    /**
     * Validates whether a candidate number can be placed at a specific position.
     *
     * @param row the row index
     * @param col the column index
     * @param candidate the number to validate
     * @return true if the number is valid at this position, false otherwise
     */
    boolean isValid(int row, int col, int candidate);

    /**
     * Regenerates the board with a new puzzle.
     */
    void regenerateBoard();

    /**
     * Locks a specific cell, preventing it from being modified.
     *
     * @param row the row index of the cell to lock
     * @param col the column index of the cell to lock
     */
    void lockCell(int row, int col);

    /**
     * Unlocks a specific cell, allowing it to be modified.
     *
     * @param row the row index of the cell to unlock
     * @param col the column index of the cell to unlock
     */
    void unlockCell(int row, int col);

    /**
     * Checks if a specific cell is locked.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return true if the cell is locked, false otherwise
     */
    boolean isCellLocked(int row, int col);

    /**
     * Unlocks all cells that are currently empty.
     */
    void unlockEmptyCells();

    /**
     * Clears the entire board.
     */
    void cleanBoard();

    /**
     * Gets the current board state.
     *
     * @return the board as a List of Lists of Integers
     */
    List<List<Integer>> getBoard();

    /**
     * Gets a suggestion for a specific cell.
     *
     * @param row the row index
     * @param col the column index
     * @return the suggested number, or 0 if no valid suggestion exists
     */
    int getSuggestion(int row, int col);
}