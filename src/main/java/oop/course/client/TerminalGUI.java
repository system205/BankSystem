package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import oop.course.client.views.IView;
import oop.course.client.views.LoginView;

import java.io.IOException;

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
