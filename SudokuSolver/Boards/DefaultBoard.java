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

    public DefaultBoard(int[][] board) {
        this.board = board;
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
        int boxRow = move.row / boxHeight;
        int boxCol = move.col / boxWidth;
        for(int i = boxRow * boxHeight; i < boxRow * boxHeight + 3; i++) {  // Todo: Test this
            for(int j = boxCol * 3; boxWidth < boxCol * boxWidth + 3; j++) {
                if(board[i][j] == move.value) {
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
                builder.append(board[i][j]);
                builder.append(" ");
            }
            builder.append("\n");
        }
        
        return builder.toString();
    }
}
