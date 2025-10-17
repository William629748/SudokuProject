package com.example.demosudoku.model.game;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Adapter class that implements the IGame interface and delegates
 * all operations to a Game instance.
 * This adapter provides a layer of abstraction between the game interface
 * and its concrete implementation.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class GameAdapter implements IGame {

    /**
     * The Game instance that handles the actual game operations.
     */
    private final Game game;

    /**
     * Constructs a new GameAdapter with a new Game instance.
     *
     * @param boardGridpane the GridPane that will contain the game board
     */
    public GameAdapter(GridPane boardGridpane) {
        this.game = new Game(boardGridpane);
    }

    /**
     * Starts the game.
     */
    @Override
    public void startGame() {
        game.startGame();
    }

    /**
     * Gets the TextField at a specific position.
     *
     * @param row the row index
     * @param col the column index
     * @return the TextField at the specified position
     */
    @Override
    public TextField getTextFieldAt(int row, int col) {
        return game.getTextFieldAt(row, col);
    }

    /**
     * Gets the SuggestionEngine for this game.
     *
     * @return the SuggestionEngine instance
     */
    @Override
    public Game.SuggestionEngine getSuggestionEngine() {
        return game.getSuggestionEngine();
    }

    /**
     * Checks if the board is complete and valid.
     *
     * @return true if the board is complete and valid, false otherwise
     */
    @Override
    public boolean isBoardComplete() {
        return game.isBoardComplete();
    }
}