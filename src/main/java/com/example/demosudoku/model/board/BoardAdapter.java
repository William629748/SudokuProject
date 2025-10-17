package com.example.demosudoku.model.board;

import java.util.List;

/**
 * Adapter class that implements the IBoard interface and delegates
 * all operations to a Board instance.
 * This adapter provides a layer of abstraction between the board interface
 * and its concrete implementation.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class BoardAdapter implements IBoard {

    /**
     * The Board instance that handles the actual board operations.
     */
    private final Board board;

    /**
     * Constructs a new BoardAdapter with a new Board instance.
     */
    public BoardAdapter() {
        this.board = new Board();
    }

    /**
     * Validates whether a candidate number can be placed at a specific position.
     *
     * @param row the row index
     * @param col the column index
     * @param candidate the number to validate
     * @return true if the number is valid at this position, false otherwise
     */
    @Override
    public boolean isValid(int row, int col, int candidate) {
        return board.isValid(row, col, candidate);
    }

    /**
     * Regenerates the board with a new puzzle.
     */
    @Override
    public void regenerateBoard() {
        board.regenerateBoard();
    }

    /**
     * Locks a specific cell, preventing it from being modified.
     *
     * @param row the row index of the cell to lock
     * @param col the column index of the cell to lock
     */
    @Override
    public void lockCell(int row, int col) {
        board.lockCell(row, col);
    }

    /**
     * Unlocks a specific cell, allowing it to be modified.
     *
     * @param row the row index of the cell to unlock
     * @param col the column index of the cell to unlock
     */
    @Override
    public void unlockCell(int row, int col) {
        board.unlockCell(row, col);
    }

    /**
     * Checks if a specific cell is locked.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return true if the cell is locked, false otherwise
     */
    @Override
    public boolean isCellLocked(int row, int col) {
        return board.isCellLocked(row, col);
    }

    /**
     * Unlocks all cells that are currently empty.
     */
    @Override
    public void unlockEmptyCells() {
        board.unlockEmptyCells();
    }

    /**
     * Clears the entire board.
     */
    @Override
    public void cleanBoard() {
        board.cleanBoard();
    }

    /**
     * Gets the current board state.
     *
     * @return the board as a List of Lists of Integers
     */
    @Override
    public List<List<Integer>> getBoard() {
        return board.getBoardAsList();
    }

    /**
     * Gets a suggestion for a specific cell.
     *
     * @param row the row index
     * @param col the column index
     * @return the suggested number, or 0 if no valid suggestion exists
     */
    @Override
    public int getSuggestion(int row, int col) {
        return board.getSuggestion(row, col);
    }
}