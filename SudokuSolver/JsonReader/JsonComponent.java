package SudokuSolver.JsonReader;

import java.io.File;
import java.io.PushbackReader;
import java.io.IOException;

public class JsonComponent {
    public static JsonComponent readJsonComponent(PushbackReader reader, char next) throws IOException {
        // Read a character of some type
        if (next == '{') {
            return new JsonMapComponent(reader); // Read "key": value}
        } else if (next == '[') {
            return new JsonArrayComponent(reader); // Read "a", "b", "c"]
        } else if (next == '"') {
            return new JsonStringComponent(reader);  // Read a string"
        } else if (next == 't' || next == 'f') {
            return new JsonBooleanComponent(reader);  // Read rue or alse
        } else if (next == 'n') {
            return new JsonNullComponent(reader); // Read ull
        } else if (next == '-' || Character.isDigit(next)) {
            return new JsonNumberComponent(reader, next);  // Read a number
        } else if (next == ' ' || next == '\n' || next == '\t' || next == '\r') {
            return readJsonComponent(reader, (char) reader.read());
        } else {
            throw new RuntimeException("Invalid JSON: " + next);
        }
 
    }

    public JsonComponent readJsonComponent(PushbackReader reader) throws IOException {
        char next = (char) reader.read();
        return readJsonComponent(reader, next);
    }
}
