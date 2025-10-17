package com.example.demosudoku.model.game;

import com.example.demosudoku.model.board.BoardAdapter;
import com.example.demosudoku.model.board.IBoard;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

/**
 * Abstract base class for game implementations.
 * Provides common fields and default implementations for the IGame interface.
 * Concrete game classes should extend this class and override methods as needed.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public abstract class GameAbstract implements IGame {

    /**
     * The GridPane that contains the visual board.
     */
    protected GridPane boardGridpane;

    /**
     * The board model that handles game logic.
     */
    protected IBoard board;

    /**
     * List of all TextField elements representing cells on the board.
     */
    protected ArrayList<TextField> numberFields;

    /**
     * Constructs a GameAbstract with the specified GridPane.
     * Initializes the board adapter and numberFields list.
     *
     * @param boardGridpane the GridPane that will contain the game board
     */
    public GameAbstract(GridPane boardGridpane) {
        this.boardGridpane = boardGridpane;
        this.board = new BoardAdapter();
        this.numberFields = new ArrayList<>();
    }

    /**
     * Starts the game. Default implementation does nothing.
     * Subclasses should override this method to provide game initialization logic.
     */
    @Override
    public void startGame() {}

    /**
     * Gets the TextField at a specific position. Default implementation returns null.
     * Subclasses should override this method to provide actual retrieval logic.
     *
     * @param row the row index
     * @param col the column index
     * @return null in this default implementation
     */
    @Override
    public TextField getTextFieldAt(int row, int col) {
        return null;
    }

    /**
     * Gets the SuggestionEngine for this game. Default implementation returns null.
     * Subclasses should override this method to provide a concrete SuggestionEngine.
     *
     * @return null in this default implementation
     */
    @Override
    public Game.SuggestionEngine getSuggestionEngine() {
        return null;
    }

    /**
     * Checks if the board is complete. Default implementation returns false.
     * Subclasses should override this method to provide actual completion checking logic.
     *
     * @return false in this default implementation
     */
    @Override
    public boolean isBoardComplete() {
        return false;
    }
}