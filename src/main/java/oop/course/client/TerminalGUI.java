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
        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
    }

    @Override
    public void startLooping(ServerBridge serverBridge) {
        try {
            // Starting
            this.screen.startScreen();

            // Main loop
            new GUIOrchestrator(
                    screen,
                    serverBridge
            ).mainLoop();

            // Closing
            this.screen.stopScreen();
            this.terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
