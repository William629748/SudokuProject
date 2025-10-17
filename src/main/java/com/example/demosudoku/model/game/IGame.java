package com.example.demosudoku.model.game;

import javafx.scene.control.TextField;

/**
 * Interface defining the contract for game operations.
 * Provides methods for starting the game, accessing board cells,
 * getting suggestions, and checking game completion.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public interface IGame {

    /**
     * Starts the game by initializing the board and setting up the UI.
     */
    void startGame();

    /**
     * Gets the TextField at a specific position on the board.
     *
     * @param row the row index
     * @param col the column index
     * @return the TextField at the specified position
     */
    TextField getTextFieldAt(int row, int col);

    /**
     * Gets the SuggestionEngine for providing hints to the player.
     *
     * @return the SuggestionEngine instance
     */
    Game.SuggestionEngine getSuggestionEngine();

    /**
     * Checks if the board is completely filled and valid.
     *
     * @return true if the board is complete and valid, false otherwise
     */
    boolean isBoardComplete();
}