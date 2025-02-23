package SudokuSolver.JsonReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;

public class JsonMapComponent extends JsonComponent {
    private HashMap<String, JsonComponent> value;
    
    public JsonMapComponent(PushbackReader reader) throws IOException {
        value = new HashMap<>();

        while (true) {
            char next = (char) reader.read();
            if (next == '"') {
                // Read the key
                StringBuilder key = new StringBuilder();
                while (true) {
                    char c = (char) reader.read();
                    if (c == '"') {
                        break;
                    }
                    key.append(c);
                }
                while (true) {
                    next = (char) reader.read();
                    if (next == ' ' || next == '\n' || next == '\t' || next == '\r' || next == ':') {
                        continue;
                    } else {
                        break;
                    }
                }
                JsonComponent value = readJsonComponent(reader, next);
                this.value.put(key.toString(), value);
            } else if (next == '}') {
                break;
            }
        }
    }

    public HashMap<String, JsonComponent> getHashMap() {
        return value;
    }

    public JsonComponent get(String key) {
        return value.get(key);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (String key : value.keySet()) {
            builder.append("\"");
            builder.append(key);
            builder.append("\": ");
            builder.append(value.get(key).toString());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }
}
