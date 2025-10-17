package com.example.demosudoku.model.board;

import java.util.*;

public class Board {

    private static final int SIZE = 6;
    private static final int BLOCK_ROWS = 2;
    private static final int BLOCK_COLS = 3;
    private static final int NUMBERS_TO_REMOVE = 24; // 36 celdas - 12 pistas = 24 a remover

    private final List<List<Integer>> board;
    private final List<List<Integer>> solution;
    private final boolean[][] lockedCells;
    private final Random random = new Random();

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

    public void initializeBoard() {
        // Limpiar tableros
        cleanBoard();

        // Generar nuevo tablero con solución única
        generatePuzzleWithUniqueSolution();
    }

    // Generar rompecabezas con solución única
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

    // Generar una solución completa válida
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

    // Algoritmo de backtracking para llenar la solución
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

    // Validar número en la solución
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

    // Copiar solución al tablero de juego
    private void copySolutionToBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board.get(i).set(j, solution.get(i).get(j));
            }
        }
    }

    // Remover números manteniendo solución única
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

    // Verificar si el tablero tiene solución única
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

    // Contar número de soluciones
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

    // Bloquear celdas con números
    private void lockFilledCells() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                lockedCells[i][j] = (board.get(i).get(j) != 0);
            }
        }
    }

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

    // Método para verificar si el tablero actual coincide con la solución
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

    public void regenerateBoard() {
        initializeBoard();
    }

    public void lockCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            lockedCells[row][col] = true;
        }
    }

    public void unlockCell(int row, int col) {
        if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
            lockedCells[row][col] = false;
        }
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
            for (int j = 0; j < SIZE; j++) {
                board.get(i).set(j, 0);
            }
        }
    }

    public List<List<Integer>> getBoardAsList() {
        return board;
    }
}