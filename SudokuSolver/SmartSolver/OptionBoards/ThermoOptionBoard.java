package SudokuSolver.SmartSolver.OptionBoards;

import java.util.LinkedList;

import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.SudokuMove;
import SudokuSolver.Boards.ThermoSudokuBoard;
import SudokuSolver.SmartSolver.OptionBoardMove;

public class ThermoOptionBoard extends DefaultOptionBoard {
    private ThermoSudokuBoard board;

    public ThermoOptionBoard(ThermoSudokuBoard board) {
        super(board);
        this.board = board;
    }

    @Override
    public OptionBoardMove setValue(int row, int col, int value) {
        // Default implementation
        LinkedList<int[]> changes = new LinkedList<>();

        // Set the other options in the row and column to 0
        for (int i = 0; i < options.length; i++) {
            if (options[i][col][value - 1] == 1) changes.add(new int[] {i, col, value - 1});
            if (options[row][i][value - 1] == 1) changes.add(new int[] {row, i, value - 1});
            
            options[i][col][value - 1] = 0;
            options[row][i][value - 1] = 0;
        }

        // Set the other options in the box to 0
        int boxRow = row / boxHeight;
        int boxCol = col / boxWidth;
        for (int i = boxRow * boxHeight; i < boxRow * boxHeight + boxHeight; i++) {
            for (int j = boxCol * boxWidth; j < boxCol * boxWidth + boxWidth; j++) {
                if (options[i][j][value - 1] == 1) {
                    changes.add(new int[] {i, j, value - 1});
                }

                options[i][j][value - 1] = 0;
            }
        }

        // Thermo implementation
        for (int i = 0; i < board.getPaths().size(); i++) {
            LinkedList<int[]> path = board.getPaths().get(i);
            for (int j = 0; j < path.size(); j++) {
                int[] ccell = path.get(j);
                if (ccell[0] == row && ccell[1] == col) {
                    if (value <= j) {
                        return new OptionBoardMove(changes);
                    }

                    // Check if the value is greater that ALL previous cells and less than the next cells
                    if (j > 0) {
                        int[] pcell = path.get(j - 1);
                        if (options[pcell[0]][pcell[1]][value - 1] == 1) {
                            changes.add(new int[] {pcell[0], pcell[1], value - 1});
                            options[pcell[0]][pcell[1]][value - 1] = 0;
                        }
                    }

                    if (j < path.size() - 1) {
                        int[] ncell = path.get(j + 1);
                        if (options[ncell[0]][ncell[1]][value - 1] == 1) {
                            changes.add(new int[] {ncell[0], ncell[1], value - 1});
                            options[ncell[0]][ncell[1]][value - 1] = 0;
                        }
                    }   
                }
            }
        }
        return new OptionBoardMove(changes);
    }

    @Override
    public OptionBoardMove makeMove(SudokuMove move) {
        return setValue(move.row, move.col, move.value);
    }

    @Override
    public DefaultOptionBoard copy() {
        ThermoOptionBoard newBoard = new ThermoOptionBoard(board);
        int[][][] newOptions = new int[options.length][options[0].length][options[0][0].length];
        for (int i = 0; i < options.length; i++) {
            for (int j = 0; j < options[0].length; j++) {
                for (int k = 0; k < options[0][0].length; k++) {
                    newOptions[i][j][k] = options[i][j][k];
                }
            }
        }
        newBoard.options = newOptions;
        return newBoard;
    }
}
