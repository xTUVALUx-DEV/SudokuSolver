import java.io.File;
import java.io.IOException;

import SudokuSolver.Boards.DefaultBoard;
import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.Boards.ThermoSudokuBoard;
import SudokuSolver.JsonReader.JsonMapComponent;
import SudokuSolver.JsonReader.JsonReader;
import SudokuSolver.SmartSolver.GenericSmartSolver;
import SudokuSolver.SmartSolver.SmartSolverConfig;

class Main {

  public void runCli(String[] args) {
      if(args[0].endsWith(".json")) {
          try {
              JsonReader reader = new JsonReader(new File(args[0]));
              JsonMapComponent json = (JsonMapComponent) reader.readJson();
              
              SudokuBoard board = new DefaultBoard(json);
              
              boolean threading = true;
              boolean transposition = true;

              for (int i = 1; i < args.length; i++) {
                  if (args[i].equals("--no-threading")) {
                      threading = false;
                  }
                  else if (args[i].equals("--no-transposition")) {
                      transposition = false;
                  }
                  else if (args[i].equals("--thermo")) {
                      board = new ThermoSudokuBoard(json);
                  }
              }
              
              GenericSmartSolver solver = new GenericSmartSolver(SmartSolverConfig.getCustomConfig(
                threading ? 1 : 0, 
                transposition, 10));
          
              solver.solve(board);
          } catch (IOException e) {
              e.printStackTrace();
          }
      } else {
          System.out.println("Invalid file type");
      }
  } 

  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
        new Main().runCli(args);
        return;
    }

    System.out.println("Usage: java Main file.json [--no-threading] [--no-transposition]");
    System.exit(0);

    JsonReader reader = new JsonReader(new File("test.json"));
    JsonMapComponent json = (JsonMapComponent) reader.readJson();
    
    SudokuBoard board = new DefaultBoard(json);
    
    GenericSmartSolver solver = new GenericSmartSolver(SmartSolverConfig.getCustomConfig(1, true, 10));

    solver.solve(board);

    // System.out.println("["+optionBoard.getOptionsReadable(0, 0) + "]");

    //BruteForceSolver solver = new BruteForceSolver();
    //solver.solve(board);
    //solver.test();


  }
}
