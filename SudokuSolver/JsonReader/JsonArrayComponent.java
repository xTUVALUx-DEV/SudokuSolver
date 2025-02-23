package SudokuSolver.JsonReader;

import java.io.PushbackReader;
import java.io.IOException;
import java.util.LinkedList;

public class JsonArrayComponent extends JsonComponent {
    private LinkedList<JsonComponent> components;

    public JsonArrayComponent(PushbackReader reader) throws IOException {
        components = new LinkedList<JsonComponent>();
        while (true) {
            char next = (char) reader.read();
            if (next == ']') {
                break;
            }
            if(next == ',' || next == ' ' || next == '\n' || next == '\t' || next == '\r') {
                continue;
            }
            JsonComponent component = readJsonComponent(reader, next);
            components.add(component);
        }
    }

    public LinkedList<JsonComponent> getValue() {
        return components;
    }
        
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (JsonComponent component : components) {
            builder.append(component.toString());
            builder.append(", ");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }

}
