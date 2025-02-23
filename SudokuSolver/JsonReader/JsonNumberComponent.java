package SudokuSolver.JsonReader;

import java.io.File;
import java.io.PushbackReader;
import java.io.IOException;

public class JsonNumberComponent extends JsonComponent {
    private int number;

    public JsonNumberComponent(PushbackReader reader, char next) throws IOException {
        StringBuilder builder = new StringBuilder();
        while (true) {
            if (next > '9' || next < '0') {
                reader.unread(next);
                break;
            }
            builder.append(next);
            next = (char) reader.read();
        }
        number = Integer.parseInt(builder.toString());
    }

    public int getValue() {
        return number;
    }

    @Override
    public String toString() {
        return Integer.toString(number);
    }
    
}
