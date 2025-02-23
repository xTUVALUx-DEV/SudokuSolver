package SudokuSolver.JsonReader;

import java.io.PushbackReader;
import java.io.IOException;

public class JsonNullComponent extends JsonComponent {
    public JsonNullComponent(PushbackReader file) throws IOException {
        file.read();
        file.read();
        file.read();
    }
    
    @Override
    public String toString() {
        return "null";
    }
}
