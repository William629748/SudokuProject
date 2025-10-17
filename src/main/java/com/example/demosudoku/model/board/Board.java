package com.example.demosudoku.model.board;

import java.util.*;

/**
 * Represents a 6x6 Sudoku board with 2x3 blocks.
 * This class handles board generation, validation, and maintains both
 * the playable board and its complete solution.
 *
 * @author Juan Marmolejo  William May
 * @version 1.0
 */
public class Board {

    /**
     * The size of the board (6x6).
     */
    private static final int SIZE = 6;

    /**
     * The number of rows in each block (2 rows).
     */
    private static final int BLOCK_ROWS = 2;

    /**
     * The number of columns in each block (3 columns).
     */
    private static final int BLOCK_COLS = 3;

    /**
     * The number of cells to remove from the complete board to create the puzzle.
     * With 36 total cells and 12 hints, 24 cells are removed.
     */
    private static final int NUMBERS_TO_REMOVE = 24;

    /**
     * The current state of the playable board.
     */
    private final List<List<Integer>> board;

    /**
     * The complete solution to the Sudoku puzzle.
     */
    private final List<List<Integer>> solution;

    /**
     * Tracks which cells are locked (cannot be modified by the player).
     */
    private final boolean[][] lockedCells;

    /**
     * Random number generator for board generation.
     */
    private final Random random = new Random();

    /**
     * Constructs a new Board and initializes it with a generated puzzle.
     */
    public Board() {
        // Inicializar las listas con el tamaño correcto
        board = new ArrayList<>(SIZE);
        solution = new ArrayList<>(SIZE);

        // Inicializar cada fila con ceros
        for (int i = 0; i < SIZE; i++) {
            List<Integer> row = new ArrayList<>(Collections.nCopies(SIZE, 0));
            board.add(row);

            List<Integer> solutionRow = new ArrayList<>(Collections.nCopies(SIZE, 0));
            solution.add(solutionRow);
        }

        lockedCells = new boolean[SIZE][SIZE];
        initializeBoard();
    }

    /**
     * Initializes the board by cleaning it and generating a new puzzle with a unique solution.
     */
    public void initializeBoard() {
        // Limpiar tableros
        cleanBoard();

        // Generar nuevo tablero con solución única
        generatePuzzleWithUniqueSolution();
    }

    /**
     * Generates a Sudoku puzzle with a unique solution.
     * This involves generating a complete solution, copying it to the board,
     * removing numbers carefully, and locking the remaining cells.
     */
    private void generatePuzzleWithUniqueSolution() {
        // Paso 1: Generar una solución completa válida
        generateCompleteSolution();

        // Paso 2: Copiar la solución al tablero de juego
        copySolutionToBoard();

        // Paso 3: Remover números cuidadosamente manteniendo solución única
        removeNumbersSafely();

        // Paso 4: Bloquear las celdas con números
        lockFilledCells();
    }

    /**
     * Generates a complete valid Sudoku solution using backtracking.
     */
    private void generateCompleteSolution() {
        // Limpiar solución
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                solution.get(i).set(j, 0);
            }
        }

        // Llenar la solución usando backtracking
        fillSolution(0, 0);
    }

    /**
     * Recursively fills the solution board using backtracking algorithm.
     *
     * @param row the current row being filled
     * @param col the current column being filled
     * @return true if the solution is successfully filled, false otherwise
     */
    private boolean fillSolution(int row, int col) {
        if (row == SIZE) {
            return true; // Solución completa
        }

        if (col == SIZE) {
            return fillSolution(row + 1, 0);
        }

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= SIZE; i++) numbers.add(i);
        Collections.shuffle(numbers, random);

        for (int num : numbers) {
            if (isValidInSolution(row, col, num)) {
                solution.get(row).set(col, num);

                if (fillSolution(row, col + 1)) {
                    return true;
                }

                solution.get(row).set(col, 0);
            }
        }
        return false;
    }

    /**
     * Validates whether a candidate number can be placed at a specific position in the solution.
     *
     * @param row the row index
     * @param col the column index
     * @param candidate the number to validate
     * @return true if the number is valid at this position, false otherwise
     */
    private boolean isValidInSolution(int row, int col, int candidate) {
        // Validar fila
        for (int j = 0; j < SIZE; j++) {
            if (solution.get(row).get(j) == candidate) return false;
        }

        // Validar columna
        for (int i = 0; i < SIZE; i++) {
            if (solution.get(i).get(col) == candidate) return false;
        }

        // Validar bloque 2x3
        int blockStartRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int blockStartCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = blockStartRow; i < blockStartRow + BLOCK_ROWS; i++) {
            for (int j = blockStartCol; j < blockStartCol + BLOCK_COLS; j++) {
                if (solution.get(i).get(j) == candidate) return false;
            }
        }
        return true;
    }

    /**
     * Copies the complete solution to the playable board.
     */
    private void copySolutionToBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board.get(i).set(j, solution.get(i).get(j));
            }
        }
    }

    /**
     * Removes numbers from the board while maintaining a unique solution.
     * Uses a careful approach to ensure the puzzle remains solvable with exactly one solution.
     */
    private void removeNumbersSafely() {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                positions.add(new int[]{i, j});
            }
        }

        Collections.shuffle(positions, random);

        int removed = 0;
        int attempts = 0;
        int maxAttempts = SIZE * SIZE * 2;

        while (removed < NUMBERS_TO_REMOVE && attempts < maxAttempts && !positions.isEmpty()) {
            int[] pos = positions.remove(0);
            int row = pos[0];
            int col = pos[1];

            if (board.get(row).get(col) != 0) {
                int backup = board.get(row).get(col);
                board.get(row).set(col, 0);

                // Verificar si todavía tiene solución única
                if (hasUniqueSolution()) {
                    removed++;
                } else {
                    // Restaurar si pierde la unicidad
                    board.get(row).set(col, backup);
                }
            }
            attempts++;
        }
    }

    /**
     * Checks if the current board has a unique solution.
     *
     * @return true if the board has exactly one solution, false otherwise
     */
    private boolean hasUniqueSolution() {
        // Hacer una copia del tablero actual
        List<List<Integer>> boardCopy = new ArrayList<>();
        for (int i = 0; i < SIZE; i++) {
            boardCopy.add(new ArrayList<>(board.get(i)));
        }

        // Contar soluciones
        int solutionCount = countSolutions(0, 0);

        // Restaurar el tablero
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board.get(i).set(j, boardCopy.get(i).get(j));
            }
        }

        return solutionCount == 1;
    }

    /**
     * Counts the number of solutions for the current board state.
     * Stops counting after finding more than one solution for efficiency.
     *
     * @param row the current row being checked
     * @param col the current column being checked
     * @return the number of solutions found (stops at 2 for efficiency)
     */
    private int countSolutions(int row, int col) {
        if (row == SIZE) {
            return 1; // Solución encontrada
        }

        if (col == SIZE) {
            return countSolutions(row + 1, 0);
        }

        if (board.get(row).get(col) != 0) {
            return countSolutions(row, col + 1);
        }

        int count = 0;
        for (int num = 1; num <= SIZE; num++) {
            if (isValid(row, col, num)) {
                board.get(row).set(col, num);

                count += countSolutions(row, col + 1);

                // Si encontramos más de una solución, podemos parar
                if (count > 1) {
                    board.get(row).set(col, 0);
                    return count;
                }

                board.get(row).set(col, 0);
            }
        }
        return count;
    }

    /**
     * Locks all cells that currently contain numbers, preventing them from being modified.
     */
    private void lockFilledCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                lockedCells[i][j] = (board.get(i).get(j) != 0);
            }
        }
    }

    /**
     * Gets a suggestion for a specific cell.
     * Returns the correct number from the solution if available,
     * otherwise returns a valid candidate number.
     *
     * @param row the row index
     * @param col the column index
     * @return the suggested number, or 0 if no valid suggestion exists
     */
    public int getSuggestion(int row, int col) {
        if (board.get(row).get(col) != 0) return 0;

        // Verificar si tenemos una solución almacenada
        if (!solution.isEmpty() && solution.get(row).get(col) != 0) {
            return solution.get(row).get(col);
        }

        // Fallback: usar el método antiguo
        List<Integer> possible = new ArrayList<>();
        for (int n = 1; n <= SIZE; n++) {
            if (isValid(row, col, n)) possible.add(n);
        }
        if (possible.isEmpty()) return 0;
        return possible.get(random.nextInt(possible.size()));
    }

    /**
     * Checks if the current board matches the complete solution.
     *
     * @return true if all cells match the solution, false otherwise
     */
    public boolean isCorrectSolution() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board.get(i).get(j) != solution.get(i).get(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Validates whether a candidate number can be placed at a specific position on the board.
     * Checks row, column, and block constraints.
     *
     * @param row the row index
     * @param col the column index
     * @param candidate the number to validate
     * @return true if the number is valid at this position, false otherwise
     */
    public boolean isValid(int row, int col, int candidate) {
        // Validar fila
        for (int j = 0; j < SIZE; j++) {
            if (board.get(row).get(j) == candidate) return false;
        }

        // Validar columna
        for (int i = 0; i < SIZE; i++) {
            if (board.get(i).get(col) == candidate) return false;
        }

        // Validar bloque 2x3
        int blockStartRow = (row / BLOCK_ROWS) * BLOCK_ROWS;
        int blockStartCol = (col / BLOCK_COLS) * BLOCK_COLS;
        for (int i = blockStartRow; i < blockStartRow + BLOCK_ROWS; i++) {
            for (int j = blockStartCol; j < blockStartCol + BLOCK_COLS; j++) {
                if (board.get(i).get(j) == candidate) return false;
            }
        }
        return true;
    }

    /**
     * Regenerates the board with a new puzzle and solution.
     */
    public void regenerateBoard() {
        initializeBoard();
    }

    /**
     * Locks a specific cell, preventing it from being modified.
     *
     * @param row the row index of the cell to lock
     * @param col the column index of the cell to lock
     */
    public void lockCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            lockedCells[row][col] = true;
        }
    }

    /**
     * Unlocks a specific cell, allowing it to be modified.
     *
     * @param row the row index of the cell to unlock
     * @param col the column index of the cell to unlock
     */
    public void unlockCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            lockedCells[row][col] = false;
        }
    }

    /**
     * Checks if a specific cell is locked.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return true if the cell is locked, false otherwise
     */
    public boolean isCellLocked(int row, int col) {
        return lockedCells[row][col];
    }

    /**
     * Unlocks all cells that are currently empty (contain 0).
     */
    public void unlockEmptyCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board.get(i).get(j) == 0) lockedCells[i][j] = false;
            }
        }
    }

    /**
     * Clears the entire board, unlocking all cells and setting all values to 0.
     */
    public void cleanBoard() {
        for (int i = 0; i < SIZE; i++) {
            Arrays.fill(lockedCells[i], false);
            for (int j = 0; j < SIZE; j++) {
                board.get(i).set(j, 0);
            }
        }
    }

    /**
     * Gets the current board as a List of Lists.
     *
     * @return the board representation
     */
    public List<List<Integer>> getBoardAsList() {
        return board;
    }
}