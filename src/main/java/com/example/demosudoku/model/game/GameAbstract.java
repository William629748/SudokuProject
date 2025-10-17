package com.example.demosudoku.model.game;

import com.example.demosudoku.model.board.BoardAdapter;
import com.example.demosudoku.model.board.IBoard;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.ArrayList;

public abstract class GameAbstract implements IGame {
    protected GridPane boardGridpane;
    protected IBoard board;
    protected ArrayList<TextField> numberFields;

    public GameAbstract(GridPane boardGridpane) {
        this.boardGridpane = boardGridpane;
        this.board = new BoardAdapter();
        this.numberFields = new ArrayList<>();
    }

    @Override
    public void startGame() {}
    @Override
    public TextField getTextFieldAt(int row, int col) { return null; }
    @Override
    public Game.SuggestionEngine getSuggestionEngine() { return null; }
    @Override
    public boolean isBoardComplete() { return false; }
}
