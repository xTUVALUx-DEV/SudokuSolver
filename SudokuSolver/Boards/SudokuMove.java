package SudokuSolver.Boards;

public class SudokuMove {
    public int row;
    public int col;
    public int value;

    SudokuMove(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }
}
