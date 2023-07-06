package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import oop.course.client.responses.BasicResponse;
import oop.course.client.views.*;
import oop.course.client.requests.Request;

import java.io.IOException;
import java.util.function.Function;

public class GUIOrchestrator {
    private IView currentView;
    private boolean changePending;
    private boolean exitCondition;

    private final WindowBasedTextGUI textGUI;
    private final Function<Request, BasicResponse> requestHandler;

    public GUIOrchestrator(Screen screen, Function<Request, BasicResponse> requestHandler) {
        currentView = new LoginView(this::changeView, requestHandler);
        changePending = true;
        textGUI = new MultiWindowTextGUI(screen);
        this.requestHandler = requestHandler;
        exitCondition = false;
    }

    public void mainLoop()
    {
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
        if (type == null) {
            exitCondition = true;
        }
        changePending = true;
        currentView = type;
    }
}
