import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import SudokuSolver.Boards.DefaultBoard;
import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.SudokuMove;
import SudokuSolver.BruteForceSolver.BruteForceSolver;
import SudokuSolver.JsonReader.JsonArrayComponent;
import SudokuSolver.JsonReader.JsonComponent;
import SudokuSolver.JsonReader.JsonMapComponent;
import SudokuSolver.JsonReader.JsonReader;
import SudokuSolver.SmartSolver.OptionBoard;
import SudokuSolver.SmartSolver.SmartSolver;

class Main {
  public static void main(String[] args) throws IOException {

    JsonReader reader = new JsonReader(new File("test.json"));
    JsonMapComponent json = (JsonMapComponent) reader.readJson();
    
    SudokuBoard board = new DefaultBoard(json);
    System.out.println(board);
    
    SmartSolver solver = new SmartSolver();
    solver.solve(board);

    // System.out.println("["+optionBoard.getOptionsReadable(0, 0) + "]");

    //BruteForceSolver solver = new BruteForceSolver();
    //solver.solve(board);
    //solver.test();


  }
}
