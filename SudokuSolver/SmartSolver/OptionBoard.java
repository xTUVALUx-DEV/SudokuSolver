package SudokuSolver.SmartSolver;

import java.util.LinkedList;

import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.SudokuMove;

public class OptionBoard {
    private int[][][] options;
    private int boxWidth;
    private int boxHeight;

    public OptionBoard(int[][][] options, int boxWidth, int boxHeight) {
        this.options = options;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    public OptionBoard(SudokuBoard board) {
        boxWidth = board.getBoxWidth();
        boxHeight = board.getBoxHeight();
        options = new int[board.getWidth()][board.getHeight()][board.getWidth()];
        for (int i = 0; i < board.getWidth(); i++) {
            for (int j = 0; j < board.getHeight(); j++) {
                if (board.getCell(i, j) == 0) {
                    for (int k = 0; k < board.getWidth(); k++) {
                        if (board.isMoveValid(new SudokuMove(i, j, k + 1))) {
                            options[i][j][k] = 1;
                        }
                    }
                } else {
                    for (int k = 0; k < board.getWidth(); k++) {
                        options[i][j][k] = 0;
                    }
                }
            }
        }
    }

    public int[] getOptions(int row, int col) {
        return options[row][col];
    }

    public String getOptionsReadable(int row, int col) { // Only for debugging
        String result = "";
        for (int i = 0; i < options[row][col].length; i++) {
            if (options[row][col][i] == 1) {
                result += (i + 1) + " ";
            }
        }
        return result;
    }
    
    public OptionBoardMove setValue(int row, int col, int value) {
        // Dont set so we dont have to reset them on undo
        //for (int i = 0; i < options[row][col].length; i++) {
        //    options[row][col][i] = 0;
        //}
        //options[row][col][value - 1] = 1;

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
        return new OptionBoardMove(changes);
    }

    public SudokuMove[] sortMoves(LinkedList<SudokuMove> moves) {
        // Sort the moves based on the number of options using counting-sort
        
        int[] moveOptions = new int[moves.size()];
        for (int i = 0; i < moves.size(); i++) {
            moveOptions[i] = 0;
            for (int j = 0; j < options.length; j++) {
                moveOptions[i] += options[moves.get(i).row][moves.get(i).col][j];
            }
        }

        int[] counts = new int[options.length + 1];
        for (int i = 0; i < moves.size(); i++) {
            counts[moveOptions[i]]++;
        }

        SudokuMove[] sortedMoves = new SudokuMove[moves.size()];
        for (int i = 1; i < counts.length; i++) {
            counts[i] += counts[i - 1];
        }

        for (int i = moves.size() - 1; i >= 0; i--) {
            sortedMoves[counts[moveOptions[i]] - 1] = moves.get(i);
            counts[moveOptions[i]]--;
        }

        return sortedMoves;

    }

    public OptionBoardMove makeMove(SudokuMove move) {
        return setValue(move.row, move.col, move.value);
    }

    public void undoMove(OptionBoardMove changes) {
        for (int[] change : changes.getChanges()) {
            options[change[0]][change[1]][change[2]] = 1;
        }        
    }

    public OptionBoard copy() {
        int[][][] newOptions = new int[options.length][options[0].length][options[0][0].length];
        for (int i = 0; i < options.length; i++) {
            for (int j = 0; j < options[0].length; j++) {
                for (int k = 0; k < options[0][0].length; k++) {
                    newOptions[i][j][k] = options[i][j][k];
                }
            }
        }
        return new OptionBoard(newOptions, boxWidth, boxHeight);
        
    }
    
}
