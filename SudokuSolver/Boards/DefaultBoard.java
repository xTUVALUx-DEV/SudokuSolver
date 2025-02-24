package SudokuSolver.Boards;

import java.util.LinkedList;

import SudokuSolver.JsonReader.JsonArrayComponent;
import SudokuSolver.JsonReader.JsonComponent;
import SudokuSolver.JsonReader.JsonMapComponent;
import SudokuSolver.JsonReader.JsonNumberComponent;

public class DefaultBoard implements SudokuBoard {
    private int[][] board;
    private int width;
    private int height;
    private int boxWidth;
    private int boxHeight;
    private int lastValidMoveCount = 0;

    public DefaultBoard(int width, int height, int boxWidth, int boxHeight) {
        board = new int[width][height];
        this.width = width;
        this.height = height;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    public DefaultBoard(JsonMapComponent json) {
        LinkedList<JsonComponent> dimensions = ((JsonArrayComponent) json.get("Dimension")).getValue();
        this.boxWidth = ((JsonNumberComponent)dimensions.get(0)).getValue();
        this.boxHeight = ((JsonNumberComponent)dimensions.get(1)).getValue();
        
        LinkedList<JsonComponent> board = ((JsonArrayComponent) json.get("Sudoku")).getValue();
        this.width = board.size();
        for (int i = 0; i < width; i++) {
            LinkedList<JsonComponent> row = ((JsonArrayComponent) board.get(i)).getValue();
            this.height = row.size();
            
            if (this.board == null) {
                this.board = new int[width][height];
            }
            
            for (int j = 0; j < height; j++) {
                this.board[i][j] = ((JsonNumberComponent)row.get(j)).getValue();
            }
        }
        
    }

    public DefaultBoard(int[][] board, int boxWidth, int boxHeight) {
        this.board = board;
        this.width = board.length;
        this.height = board[0].length;
        this.boxWidth = boxWidth;
        this.boxHeight = boxHeight;
    }

    @Override
    public void setCell(int row, int col, int value) {
        board[row][col] = value;
    }

    @Override
    public int getCell(int row, int col) {
        return board[row][col];
    }

    @Override
    public void printBoard() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Last Valid Move Count: " + lastValidMoveCount);
    }

    @Override
    public boolean isSolved() {
        // Check if the board is full
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isMoveValid(SudokuMove move) {
        for(int i = 0; i < width; i++) {
            if(board[move.row][i] == move.value) {
                return false;
            }
        }
        for(int i = 0; i < height; i++) {
            if(board[i][move.col] == move.value) {
                return false;
            }
        }
        
        // Get the top left corner of the box that the move is in
        int moveRow = move.row;
        int moveCol = move.col;

        int moveBoxRow = moveRow / boxHeight;
        int moveBoxCol = moveCol / boxWidth;
        
        for (int i = moveBoxRow * boxHeight; i < (moveBoxRow + 1) * boxHeight; i++) {
            for (int j = moveBoxCol * boxWidth; j < (moveBoxCol + 1) * boxWidth; j++) {
                if (board[i][j] == move.value) {
                    return false;
                }
            }
        }


        return true;
    }

    @Override
    public LinkedList<SudokuMove> getPossibleMoves() {
        LinkedList<SudokuMove> moves = new LinkedList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 1; k <= boxHeight*boxWidth; k++) {  // boxHeight*boxWidth should be the maximum number
                    if (board[i][j] == 0) {
                        // Validate Move
                        SudokuMove move = new SudokuMove(i, j, k);
                        boolean isMoveValid = isMoveValid(move);
                        if (isMoveValid) {
                            moves.add(move);
                        }
                    }
                }
            }
        }
        lastValidMoveCount = moves.size();

        return moves;
    }

    @Override
    public void makeMove(SudokuMove move) {
        board[move.row][move.col] = move.value;
    }

    @Override
    public void undoMove(SudokuMove move) {
        board[move.row][move.col] = 0;
    }
    
    @Override
    public String toString() {
        // Format the board
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                builder.append(board[i][j] == 0 ? " " : board[i][j]);
                builder.append(" ");
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    @Override
    public SudokuBoard copy() {
        int[][] newBoard = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                newBoard[i][j] = board[i][j];
            }
        }
        return new DefaultBoard(newBoard, boxWidth, boxHeight);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getBoxWidth() {
        return boxWidth;
    }

    @Override
    public int getBoxHeight() {
        return boxHeight;
    }
}
