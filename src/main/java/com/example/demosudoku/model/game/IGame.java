package com.example.demosudoku.model.game;

import javafx.scene.control.TextField;

public interface IGame {
    void startGame();
    TextField getTextFieldAt(int row, int col);
    Game.SuggestionEngine getSuggestionEngine();
    boolean isBoardComplete();
}
