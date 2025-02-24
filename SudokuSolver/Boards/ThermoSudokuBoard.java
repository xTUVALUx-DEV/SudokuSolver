package SudokuSolver.Boards;

import java.util.ArrayList;
import java.util.LinkedList;

import SudokuSolver.JsonReader.JsonArrayComponent;
import SudokuSolver.JsonReader.JsonComponent;
import SudokuSolver.JsonReader.JsonMapComponent;
import SudokuSolver.JsonReader.JsonNumberComponent;

public class ThermoSudokuBoard extends DefaultBoard {
    private ArrayList<LinkedList<int[]>> paths;

    public ThermoSudokuBoard(JsonMapComponent json) {
        super(json);
        
        LinkedList<JsonComponent> paths = ((JsonArrayComponent) json.get("Thermo")).getValue();
        this.paths = new ArrayList<LinkedList<int[]>>();

        for (int i = 0; i < paths.size(); i++) {
            LinkedList<JsonComponent> path = ((JsonArrayComponent) paths.get(i)).getValue();
            LinkedList<int[]> pathCells = new LinkedList<>();
            for (int j = 0; j < path.size(); j++) {
                LinkedList<JsonComponent> nums = ((JsonArrayComponent) path.get(j)).getValue();
                pathCells.add(new int[] {
                    ((JsonNumberComponent) nums.get(0)).getValue(),
                    ((JsonNumberComponent) nums.get(1)).getValue()
                });
            }
            this.paths.add(pathCells);
        }
    }

    public ThermoSudokuBoard(int width, int height, int boxWidth, int boxHeight) {
        super(width, height, boxWidth, boxHeight);
        this.paths = new ArrayList<LinkedList<int[]>>();
    }

    @Override
    public boolean isMoveValid(SudokuMove move) {
        if (!super.isMoveValid(move)) {
            return false;
        }
        //if(1==1) return true;

        int row = move.getRow();
        int col = move.getCol();
        int value = move.getValue();

        for (int i = 0; i < paths.size(); i++) {
            for (int j = 0; j < paths.get(i).size(); j++) {
                int[] cell = paths.get(i).get(j);

                if (cell[0] == row && cell[1] == col) {
                    if (value <= j) {
                        return false;
                    }

                    // Check if the value is greater that ALL previous cells and less than the next cells
                    if (j > 0){
                        int[] previousCell = paths.get(i).get(j - 1);
                        if (board[previousCell[0]][previousCell[1]] >= value && board[previousCell[0]][previousCell[1]] != 0) {
                            return false;
                        }
                    }
                    
                    if (j < paths.get(i).size() - 1){
                        int[] nextCell = paths.get(i).get(j + 1);
                        if (board[nextCell[0]][nextCell[1]] <= value && board[nextCell[0]][nextCell[1]] != 0) {
                            return false;
                        }
                    }

                    break;
                }
            }
        }

        return true;
    }
    
    public String toThermoString() {

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boolean isPath = false;
                if (paths.size() > 0) {
                    for (int k = 0; k < paths.size(); k++) {
                        for (int l = 0; l < paths.get(k).size(); l++) {
                            if (paths.get(k).get(l)[0] == i && paths.get(k).get(l)[1] == j) {
                                if (l == 0) {
                                    builder.append(">");
                                } else if (l == paths.get(k).size() - 1) {
                                    builder.append("<");
                                } else {
                                    builder.append("v");
                                }
                                isPath = true;
                                break;
                            }
                        }
                    }
                }

                if (!isPath) {
                    builder.append(board[i][j] == 0 ? " " : board[i][j]);
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public ThermoSudokuBoard copy() {
        ThermoSudokuBoard newBoard = new ThermoSudokuBoard(width, height, boxWidth, boxHeight);

        int[][] b = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                b[i][j] = board[i][j];
            }
        }

        newBoard.board = b;
        
        newBoard.paths = new ArrayList<LinkedList<int[]>>();
        for (int i = 0; i < this.paths.size(); i++) {
            LinkedList<int[]> path = new LinkedList<>();
            for (int j = 0; j < this.paths.get(i).size(); j++) {
                path.add(this.paths.get(i).get(j).clone());
            }
            newBoard.paths.add(path);
        }
        return newBoard;
    }
}
