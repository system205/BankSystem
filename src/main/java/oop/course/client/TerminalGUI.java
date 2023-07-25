package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import com.googlecode.lanterna.screen.*;
import com.googlecode.lanterna.terminal.*;
import oop.course.client.views.*;

import java.io.*;

public final class TerminalGUI implements GUI {
    private final Screen screen;
    private final WindowBasedTextGUI gui;

    public TerminalGUI() throws IOException {
        this.screen = new DefaultTerminalFactory().createScreen();
        this.gui = new MultiWindowTextGUI(this.screen);
        this.screen.startScreen();
    }

    @Override
    public void startLooping(ServerBridge serverBridge) {
        while (true) {
            try {
                new LoginView(this::changeView, this::exit, serverBridge).show(this.gui);
            } catch (RuntimeException e) {
                if (!this.gui.getWindows().isEmpty()) {
                    this.gui.getActiveWindow().close();
                }
                MessageDialog.showMessageDialog(this.gui, "Application Error", e.getMessage(), MessageDialogButton.Close);
            }
        }
    }

    private void changeView(IView view) {
        for (var window : this.gui.getWindows()) {
            window.close();
        }
        view.show(this.gui);
    }

    private void exit() {
        try {
            this.screen.stopScreen();
        } catch (IOException e) {
            System.exit(-1);
        }
        System.exit(0);
    }
}
