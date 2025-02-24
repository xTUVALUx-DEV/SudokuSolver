package SudokuSolver.Boards;

public class SudokuMove {
    public int row;
    public int col;
    public int value;

    public SudokuMove(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ", " + value + ")";
    }
}
