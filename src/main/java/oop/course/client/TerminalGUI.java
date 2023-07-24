package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import oop.course.client.views.IView;

import java.io.IOException;
import java.util.List;

public class TerminalGUI implements GUI {
    private final Terminal terminal;
    private final Screen screen;
    private final List<IView> views;
    private final WindowBasedTextGUI gui;

    public TerminalGUI(List<IView> views) throws IOException {
        this.terminal = new DefaultTerminalFactory().createTerminal();
        this.screen = new TerminalScreen(terminal);
        this.views = views;
        this.gui = new MultiWindowTextGUI(screen);
    }

    @Override
    public void startLooping(ServerBridge serverBridge) throws IOException {
        this.screen.startScreen();

        views.get(0).show(gui);

        this.screen.stopScreen();
        this.terminal.close();
    }
}
