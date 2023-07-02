package oop.course.client;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class TerminalGUI implements GUI {
    private final Terminal terminal;
    private final Screen screen;

    public TerminalGUI() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
    }

    @Override
    public void startLooping() {
        try {
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new GUIOrchestrator(screen).mainLoop(screen);
        try {
            screen.stopScreen();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
