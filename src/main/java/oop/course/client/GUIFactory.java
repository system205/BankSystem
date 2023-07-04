package oop.course.client;

import java.io.IOException;

public class GUIFactory {
    public GUI bestGUIImplementation() throws IOException {
        return new TerminalGUI();
    }
}
