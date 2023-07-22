package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import oop.course.client.views.IView;
import oop.course.client.views.LoginView;

import java.io.IOException;

public class GUIOrchestrator {
    private final WindowBasedTextGUI textGUI;
    private IView currentView;
    private boolean changePending;
    private boolean exitCondition;

    public GUIOrchestrator(Screen screen, ServerBridge serverBridge) {
        currentView = new LoginView(this::changeView, this::exit, serverBridge);
        changePending = true;
        textGUI = new MultiWindowTextGUI(screen);
        exitCondition = false;
    }

    public void mainLoop() {
        try {
            while (!exitCondition) {
                if (changePending) {
                    changePending = false;
                    currentView.show(textGUI);
                }
                textGUI.getGUIThread().processEventsAndUpdate();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private void changeView(IView type) {
        changePending = true;
        currentView = type;
    }

    private void exit()
    {
        exitCondition = true;
    }
}
