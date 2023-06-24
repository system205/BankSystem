package oop.course.tools.implementations;

import oop.course.tools.inerfaces.*;

import java.io.*;
import java.util.stream.*;

public class TextFromFile implements Source {
    private final String filepath;

    public TextFromFile(String filepath) {
        this.filepath = filepath;
    }

    @Override
    public String text() {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(filepath)) {
            if (stream == null)
            {
                throw new RuntimeException("File: " + filepath + " was not found or without access");
            }

            return new BufferedReader(
                    new InputStreamReader(stream)
            ).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
