import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import SudokuSolver.Boards.DefaultBoard;
import SudokuSolver.Boards.SudokuBoard;
import SudokuSolver.JsonReader.JsonArrayComponent;
import SudokuSolver.JsonReader.JsonComponent;
import SudokuSolver.JsonReader.JsonMapComponent;
import SudokuSolver.JsonReader.JsonReader;

class Main {
  public static void main(String[] args) throws IOException {

    JsonReader reader = new JsonReader(new File("test.json"));
    JsonMapComponent json = (JsonMapComponent) reader.readJson();
    
    SudokuBoard board = new DefaultBoard(json);

    System.out.println(board);

  }
}
