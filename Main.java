import java.io.File;
import java.io.IOException;

import SudokuSolver.Boards.DefaultBoard;
import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.ThermoSudokuBoard;
import SudokuSolver.JsonReader.JsonMapComponent;
import SudokuSolver.JsonReader.JsonReader;
import SudokuSolver.SmartSolver.GenericSmartSolver;

class Main {
  public static void main(String[] args) throws IOException {

    JsonReader reader = new JsonReader(new File("thermo.json"));
    JsonMapComponent json = (JsonMapComponent) reader.readJson();
    
    SudokuBoard board = new ThermoSudokuBoard(json);
    System.out.println(board);
    
    GenericSmartSolver solver = new GenericSmartSolver();


    solver.solve(board);

    // System.out.println("["+optionBoard.getOptionsReadable(0, 0) + "]");

    //BruteForceSolver solver = new BruteForceSolver();
    //solver.solve(board);
    //solver.test();


  }
}
