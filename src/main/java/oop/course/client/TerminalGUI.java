package oop.course.client;

import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;
import oop.course.client.requests.*;
import oop.course.client.responses.*;

import java.io.*;
import java.util.function.*;

public class TerminalGUI implements GUI {
    private final Terminal terminal;
    private final Screen screen;

    public TerminalGUI() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
    }

    @Override
    public void startLooping(Function<Request, BasicResponse> requestHandler) {
        try {
            screen.startScreen();
            new GUIOrchestrator(
                    screen,
                    requestHandler
            ).mainLoop();
            screen.stopScreen();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
