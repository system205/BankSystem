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
    public void startLooping(ServerBridge serverBridge) throws IOException {
        this.screen.startScreen();

        while (true) {
            try {
                new GUIOrchestrator(
                        this.screen,
                        serverBridge
                ).mainLoop();
            }
            catch (RuntimeException e) {
                System.err.println("Connection error while sending the request.");
            }
        }

        /*this.screen.stopScreen();
        this.terminal.close();*/
    }
}
