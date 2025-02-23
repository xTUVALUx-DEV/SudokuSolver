package SudokuSolver.JsonReader;

import java.io.PushbackReader;
import java.io.IOException;

public class JsonStringComponent extends JsonComponent {
    private String value;

    public JsonStringComponent(PushbackReader reader) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (true) {
            char next = (char) reader.read();
            if (next == '"') {
                break;
            }
            builder.append(next);
        }
        value = builder.toString();
    }
    
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }
}
