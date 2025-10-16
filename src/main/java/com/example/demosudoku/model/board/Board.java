package com.example.demosudoku.model.board;

import java.util.*;

public class Board {

    private static final int SIZE = 6;
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLS = 3;
    private static final int NUMBERS_PER_BLOCK = 2;

    private final List<List<Integer>> board;
    private final boolean[][] lockedCells;
    private final Random random = new Random();

    public Board() {
        board = new ArrayList<>();
        lockedCells = new boolean[SIZE][SIZE];
        initializeBoard();
    }

    public void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>(Collections.nCopies(SIZE, 0));
            board.add(row);
            Arrays.fill(lockedCells[i], false);
        }
        fillBlocks(0);
    }

    private boolean fillBlocks(int blockIndex) {
        if (blockIndex >= (SIZE / BLOCK_ROWS) * (SIZE / BLOCK_COLS)) {
            return true;
        }

        int blockRow = (blockIndex / (SIZE / BLOCK_COLS)) * BLOCK_ROWS;
        int blockCol = (blockIndex % (SIZE / BLOCK_COLS)) * BLOCK_COLS;

        List<int[]> blockCells = new ArrayList<>();
        for (int i = blockRow; i < blockRow + BLOCK_ROWS; i++) {
            for (int j = blockCol; j < blockCol + BLOCK_COLS; j++) {
                blockCells.add(new int[]{i, j});
            }
        }
        // Barajar las celdas del bloque
        Collections.shuffle(blockCells, random);

        int placed = 0;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) numbers.add(i);
        Collections.shuffle(numbers, random);

        for (int[] cell : blockCells) {
            for (int num : numbers) {
                if (isValid(cell[0], cell[1], num)) {
                    board.get(cell[0]).set(cell[1], num);
                    lockCell(cell[0], cell[1]);
                    placed++;
                    if (placed == NUMBERS_PER_BLOCK) {
                        if (fillBlocks(blockIndex + 1)) return true;
                        break;
                    }
                }
            }
        }
        return false;
    }

    public boolean isValid(int row, int col, int candidate) {
        for (int j = 0; j < SIZE; j++) {
            if (board.get(row).get(j) == candidate) return false;
        }
        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == candidate) return false;
        }

        int blockStartRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int blockStartCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = blockStartRow; i < blockStartRow + BLOCK_ROWS; i++) {
            for (int j = blockStartCol; j < blockStartCol + BLOCK_COLS; j++) {
                if (board.get(i).get(j) == candidate) return false;
            }
        }
        return true;
    }

    public void regenerateBoard() {
        cleanBoard();
        fillBlocks(0);
    }

    public void lockCell(int row, int col) {
        lockedCells[row][col] = true;
    }

    public void unlockCell(int row, int col) {
        lockedCells[row][col] = false;
    }

    public boolean isCellLocked(int row, int col) {
        return lockedCells[row][col];
    }

    public void unlockEmptyCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board.get(i).get(j) == 0) lockedCells[i][j] = false;
            }
        }
    }

    public void cleanBoard() {
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(lockedCells[i], false);
            for (int j = 0; j < SIZE; j++) board.get(i).set(j, 0);
        }
    }

    public List<List<Integer>> getBoardAsList() {
        return board;
    }

    public int getSuggestion(int row, int col) {
        if (board.get(row).get(col) != 0) return 0;
        List<Integer> possible = new ArrayList<>();
        for (int n = 1; n <= SIZE; n++) {
            if (isValid(row, col, n)) possible.add(n);
        }
        if (possible.isEmpty()) return 0;
        return possible.get(random.nextInt(possible.size()));
    }
}
