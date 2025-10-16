package com.example.demosudoku.model.game;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class GameAdapter implements IGame {
    private final Game game;

    public GameAdapter(GridPane boardGridpane) {
        this.game = new Game(boardGridpane);
    }

    @Override
    public void startGame() { game.startGame(); }

    @Override
    public TextField getTextFieldAt(int row, int col) {
        return game.getTextFieldAt(row, col);
    }

    @Override
    public Game.SuggestionEngine getSuggestionEngine() {
        return game.getSuggestionEngine();
    }

    @Override
    public boolean isBoardComplete() {
        return game.isBoardComplete();
    }
}
