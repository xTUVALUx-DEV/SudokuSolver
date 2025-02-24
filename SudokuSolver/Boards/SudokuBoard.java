package SudokuSolver.Boards;

import java.util.LinkedList;

public interface SudokuBoard {
    
    public void setCell(int row, int col, int value);
    public int getCell(int row, int col);
    public void printBoard();
    public boolean isSolved();
    public SudokuBoard copy();

    public LinkedList<SudokuMove> getPossibleMoves();
    public boolean isMoveValid(SudokuMove move);
    public void makeMove(SudokuMove move);
    public void undoMove(SudokuMove move);
} 