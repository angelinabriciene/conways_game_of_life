package org.example;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello digital artificial life form!");

        int size = 10;
        Cell[][] grid = new Cell[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                grid[x][y] = new Cell(Math.random() > 0.5);
            }
        }

        int iterations = 0;
        Cell[][] previousGrid = new Cell[size][size];
        Cell[][] previousPreviousGrid = new Cell[size][size];

        while (true) {
            int population = countPopulation(grid);
            System.out.println("Generation: " + iterations + ", Population: " + population);
            System.out.println();

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    System.out.print(grid[x][y].toString() + "  ");
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("---------------------------------------");
            System.out.println();

            int changes = nextGeneration(grid, previousGrid, previousPreviousGrid);

            if (changes == 0) {
                System.out.println("Equilibrium 'Still lifes' reached after " + iterations + " iterations.");
                System.out.println("Good bye digital artificial life form!");
                break;
            }

            if (Arrays.deepEquals(grid, previousPreviousGrid)) {
                System.out.println("Blinker state 'Oscillators' reached after " + iterations + " iterations.");
                System.out.println("Good bye digital artificial life form!");
                break;
            }

            iterations++;

            Thread.sleep(1000);
        }
    }

    static int nextGeneration(Cell[][] grid, Cell[][] previousGrid, Cell[][] previousPreviousGrid) {
        int size = grid.length;
        Cell[][] newGrid = new Cell[size][size];
        int changes = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                boolean newState = nextState(grid, x, y);
                if (newState != grid[x][y].isFull()) {
                    changes++;
                }
                newGrid[x][y] = new Cell(newState);
            }
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                previousPreviousGrid[x][y] = previousGrid[x][y];
                previousGrid[x][y] = grid[x][y];
                grid[x][y] = newGrid[x][y];
            }
        }

        return changes;
    }

    static boolean nextState(Cell[][] grid, int x, int y) {
        int liveNeighbors = countLiveNeighbors(grid, x, y);
        if (grid[x][y].isFull()) {
            return liveNeighbors == 2 || liveNeighbors == 3;
        } else {
            return liveNeighbors == 3;
        }
    }

    static int countLiveNeighbors(Cell[][] grid, int x, int y) {
        int size = grid.length;
        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) {
                    continue;
                }
                int neighborX = x + dx;
                int neighborY = y + dy;
                if (neighborX >= 0 && neighborX < size && neighborY >= 0 && neighborY < size) {
                    if (grid[neighborX][neighborY].isFull()) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    static int countPopulation(Cell[][] grid) {
        int size = grid.length;
        int population = 0;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (grid[x][y].isFull()) {
                    population++;
                }
            }
        }

        return population;
    }
}

class Cell {
    private boolean isFull;

    public Cell(boolean isFull) {
        this.isFull = isFull;
    }

    public boolean isFull() {
        return isFull;
    }

    public String toString() {
        return isFull ? "■" : "□";
    }
}