package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import oop.course.client.views.IView;
import oop.course.client.views.LoginView;

import java.io.IOException;

public class GUIOrchestrator {
    private final WindowBasedTextGUI textGUI;
    private final ServerBridge serverBridge;
    private IView currentView;
    private boolean changePending;
    private boolean exitCondition;

    public GUIOrchestrator(Screen screen, ServerBridge serverBridge) {
        this.currentView = new LoginView(this::changeView, this::exit, serverBridge);
        this.changePending = true;
        this.textGUI = new MultiWindowTextGUI(screen);
        this.exitCondition = false;
        this.serverBridge = serverBridge;
    }

    public void mainLoop() {
        while (!this.exitCondition) {
            try {
                if (this.changePending) {
                    this.changePending = false;
                    this.currentView.show(this.textGUI);
                }
                this.textGUI.getGUIThread().processEventsAndUpdate();
            } catch (RuntimeException e) {
                if (!this.textGUI.getWindows().isEmpty()) {
                    this.textGUI.getActiveWindow().close();
                }
                MessageDialog.showMessageDialog(this.textGUI, "Application Error", e.getMessage(),
                        MessageDialogButton.Close);
                currentView = new LoginView(this::changeView, this::exit, this.serverBridge);
                currentView.show(textGUI);
            } catch (IOException e) {
                System.err.println("GUI error occured, cannot proceed.");
                System.exit(-1);
            }
        }
    }

    private void changeView(IView type) {
        changePending = true;
        currentView = type;
    }

    private void exit() {
        exitCondition = true;
    }
}
