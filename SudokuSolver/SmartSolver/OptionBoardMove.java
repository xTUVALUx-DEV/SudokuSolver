package SudokuSolver.SmartSolver;

import java.util.LinkedList;

public class OptionBoardMove {
    public LinkedList<int[]> changes;

    public OptionBoardMove(LinkedList<int[]> changes) {
        this.changes = changes;
    }
    
    public LinkedList<int[]> getChanges() {
        return changes;
    }
}
