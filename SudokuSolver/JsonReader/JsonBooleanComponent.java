package SudokuSolver.JsonReader;

import java.io.File;
import java.io.PushbackReader;
import java.io.IOException;

public class JsonBooleanComponent extends JsonComponent {
    private boolean value;
    
    public boolean getValue() {
        return value;
    }
    
    JsonBooleanComponent(PushbackReader reader) throws IOException {
        // Read the boolean value
        StringBuilder builder = new StringBuilder();
        while (true) {
            char next = (char) reader.read();
            if (next == 't' || next == 'f') {
                builder.append(next);
            } else {
                break;
            }
        }
        value = Boolean.parseBoolean(builder.toString());
    }

    @Override
    public String toString() {
        return Boolean.toString(value);
    }
    
}
