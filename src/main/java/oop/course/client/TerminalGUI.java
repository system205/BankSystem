package oop.course.client;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

import java.io.IOException;
import java.util.function.Function;

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new GUIOrchestrator(screen, requestHandler).mainLoop();
        try {
            screen.stopScreen();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
