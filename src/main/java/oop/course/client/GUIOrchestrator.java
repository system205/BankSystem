package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import oop.course.client.views.IView;
import oop.course.client.views.LoginView;

public class GUIOrchestrator {
    private final WindowBasedTextGUI textGUI;
    private final ServerBridge serverBridge;

    public GUIOrchestrator(Screen screen, ServerBridge serverBridge) {
        this.textGUI = new MultiWindowTextGUI(screen);
        this.serverBridge = serverBridge;
    }

    public void mainLoop() {
        try {
            new LoginView(this::changeView, this::exit, this.serverBridge).show(this.textGUI);
        } catch (RuntimeException e) {
            if (!this.textGUI.getWindows().isEmpty()) {
                this.textGUI.getActiveWindow().close();
            }
            MessageDialog.showMessageDialog(this.textGUI, "Application Error", e.getMessage(), MessageDialogButton.Close);
        }
    }

    private void changeView(IView view) {
        for (var window : this.textGUI.getWindows()) {
            window.close();
        }
        view.show(this.textGUI);
    }

    private void exit() {
        System.exit(0);
    }
}
