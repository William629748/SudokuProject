package com.example.demosudoku.model.game;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board {
    private final int SIZE = 6;
    private final int BLOCK_ROWS = 2;
    private final int BLOCK_COLS = 3;
    private int[][] grid;

    public Board() {
        grid = new int[SIZE][SIZE];
        initializeBoard();
    }

    private void initializeBoard() {
        clearBoard();
        fillInitialBlocks();
    }

    private void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = 0;
            }
        }
    }

    private void fillInitialBlocks() {
        Random random = new Random();

        for (int blockRow = 0; blockRow < BLOCK_ROWS; blockRow++) {
            for (int blockCol = 0; blockCol < BLOCK_COLS; blockCol++) {
                Set<Integer> used = new HashSet<>();
                int numbersAdded = 0;

                while (numbersAdded < 2) {
                    int num = random.nextInt(6) + 1;
                    int row = blockRow * BLOCK_ROWS + random.nextInt(BLOCK_ROWS);
                    int col = blockCol * BLOCK_COLS + random.nextInt(BLOCK_COLS);

                    if (grid[row][col] == 0 && isValid(row, col, num)) {
                        grid[row][col] = num;
                        used.add(num);
                        numbersAdded++;
                    }
                }
            }
        }
    }

    public boolean isValid(int row, int col, int num) {
        for (int i = 0; i < SIZE; i++) {
            if (grid[row][i] == num || grid[i][col] == num) {
                return false;
            }
        }

        int blockStartRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int blockStartCol = (col / BLOCK_COLS) * BLOCK_COLS;

        for (int i = 0; i < BLOCK_ROWS; i++) {
            for (int j = 0; j < BLOCK_COLS; j++) {
                if (grid[blockStartRow + i][blockStartCol + j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean setCell(int row, int col, int num) {
        if (num < 1 || num > 6) return false;
        if (!isValid(row, col, num)) return false;
        grid[row][col] = num;
        return true;
    }

    public void clearCell(int row, int col) {
        grid[row][col] = 0;
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getSuggestion(int row, int col) {
        if (grid[row][col] != 0) return grid[row][col];
        for (int num = 1; num <= 6; num++) {
            if (isValid(row, col, num)) return num;
        }
        return 0;
    }

    public boolean isFull() {
        for (int[] row : grid) {
            for (int cell : row) {
                if (cell == 0) return false;
            }
        }
        return true;
    }

    public boolean isCompleteAndValid() {
        for (int i = 0; i < SIZE; i++) {
            Set<Integer> rowSet = new HashSet<>();
            Set<Integer> colSet = new HashSet<>();

            for (int j = 0; j < SIZE; j++) {
                int rowVal = grid[i][j];
                int colVal = grid[j][i];

                if (rowVal == 0 || colVal == 0) return false;
                if (!rowSet.add(rowVal) || !colSet.add(colVal)) return false;
            }
        }

        for (int br = 0; br < BLOCK_ROWS; br++) {
            for (int bc = 0; bc < BLOCK_COLS; bc++) {
                Set<Integer> blockSet = new HashSet<>();
                for (int i = 0; i < BLOCK_ROWS; i++) {
                    for (int j = 0; j < BLOCK_COLS; j++) {
                        int val = grid[br * BLOCK_ROWS + i][bc * BLOCK_COLS + j];
                        if (val == 0 || !blockSet.add(val)) return false;
                    }
                }
            }
        }

        return true;
    }
}
