package SudokuSolver.SmartSolver;

import SudokuSolver.Boards.SudokuMove;

public class OptionsMoveWrapper {
    public SudokuMove move;
    public int optionCount;
    
    public OptionsMoveWrapper(SudokuMove move, int optionCount) {
        this.move = move;
        this.optionCount = optionCount;
    }
    
    public SudokuMove getMove() {
        return move;
    }

    public int getOptionCount() {
        return optionCount;
    }
}
