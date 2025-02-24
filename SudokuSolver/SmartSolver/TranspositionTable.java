package SudokuSolver.SmartSolver;

import SudokuSolver.Boards.SudokuBoard;

public class TranspositionTable {
    static final int SIZE = 40000000;
    static long[] hashes = new long[SIZE];

    public static boolean checkOrAdd(SudokuBoard board) {
        long hash = board.getHash();
        int index = (int) (hash % SIZE);
        if (index < 0) {
            index += SIZE;
        }
        if (hashes[index] == hash) {
            return true;
        }
        hashes[index] = hash;
        return false;
    }
}
