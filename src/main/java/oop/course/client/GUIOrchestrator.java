package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import oop.course.client.actions.Action;
import oop.course.client.responses.Response;
import oop.course.client.views.*;
import oop.course.client.requests.Request;

import java.io.IOException;
import java.util.function.Consumer;

public class GUIOrchestrator {
    private IView.Type currentView;
    private boolean changePending;

    private final WindowBasedTextGUI textGUI;
    private final Consumer<Request<Response>> requestHandler;

    public GUIOrchestrator(Screen screen, Consumer<Request<Response>> requestHandler) {
        currentView = IView.Type.Login;
        changePending = true;
        textGUI = new MultiWindowTextGUI(screen);
        this.requestHandler = requestHandler;
    }

    public void mainLoop()
    {
        try {
            currentView = IView.Type.Login;
            while (true) {
                if (changePending)
                {
                    changePending = false;
                    if (currentView == IView.Type.None)
                    {
                        break;
                    }
                    IView view = switch (currentView) {
                        case Account -> new AccountView(this::handleAction);
                        case Transfer -> new TransferView(this::handleAction);
                        case Login -> new LoginView(this::handleAction);
                        case Register -> new RegisterView(this::handleAction);
                        case ActionSelect -> new ActionSelectView(this::handleAction);
                        case None -> throw new RuntimeException();
                    };
                    view.show(textGUI);
                }
                textGUI.getGUIThread().processEventsAndUpdate();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private void changeView(IView.Type type) {
        changePending = true;
        currentView = type;
    }

    private void handleAction(Action action) {
        action.perform(this::changeView, requestHandler);
    }
}
