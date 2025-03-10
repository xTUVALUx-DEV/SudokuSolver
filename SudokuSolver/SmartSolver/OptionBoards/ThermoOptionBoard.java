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
                    for (int k = 0; k < path.size(); k++) {
                        if (k == j) { continue; }
                        if (k > j) {
                            for(int v = 0; v < value-1; v++) {
                                int[] rowCol = path.get(k);

                                if(options[rowCol[0]][rowCol[1]][v] == 1) {
                                    changes.add(new int[] {rowCol[0], rowCol[1], v});
                                    options[rowCol[0]][rowCol[1]][v] = 0;
                                }
                            }
                        }
                        else if (k < j) {
                            for(int v = value+1; v <= options[0][0].length-1; v++) {
                                int[] rowCol = path.get(k);

                                if(options[rowCol[0]][rowCol[1]][v] == 1) {
                                    changes.add(new int[] {rowCol[0], rowCol[1], v});
                                    options[rowCol[0]][rowCol[1]][v] = 0;
                                }
                            }
                        }
                    }

                }
            }
        }
        // if (!validateBoard()) {
        //     System.out.println("Move: " + row + " " + col + " " + value);
        //     System.exit(0);
        // }
        return new OptionBoardMove(changes);
    }

    
    public boolean validateBoard() {
        for (int i = 0; i < board.getPaths().size(); i++) {
            LinkedList<int[]> path = board.getPaths().get(i);
            
            int[] lastCell = path.get(0);
            int lastValue = board.getCell(lastCell[0], lastCell[1]);
            for(int[] cell : path) {
                if (cell[0] == lastCell[0] && cell[1] == lastCell[1]) {
                    continue;
                }
                if (board.getCell(cell[0], cell[1]) != 0 && board.getCell(cell[0], cell[1]) < lastValue) {
                    System.out.println("Invalid thermo path");
                    System.out.println(board);
                    return false;
                }
                lastValue = board.getCell(lastCell[0], lastCell[1]);
                lastCell = cell;
            }
        }
        return true;
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
