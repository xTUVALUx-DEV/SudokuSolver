package SudokuSolver.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.PushbackReader;
import java.io.IOException;

public class JsonReader {
    private File file;

    public JsonReader(File file) {
        this.file = file;
    }

    public JsonComponent readJson() throws IOException {
        PushbackReader reader = new PushbackReader(new FileReader(file));
        JsonComponent component = JsonComponent.readJsonComponent(reader, (char) reader.read());
        return component;
    }

}

