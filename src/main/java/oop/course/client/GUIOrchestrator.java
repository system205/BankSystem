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
    private IView.Type currentView;
    private boolean changePending;

    private final WindowBasedTextGUI textGUI;
    private final Function<Request, BasicResponse> requestHandler;

    public GUIOrchestrator(Screen screen, Function<Request, BasicResponse> requestHandler) {
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
                        case Account -> new AccountView(this::changeView, requestHandler);
                        case Transfer -> new TransferView(this::changeView, requestHandler);
                        case Login -> new LoginView(this::changeView, requestHandler);
                        case Register -> new RegisterView(this::changeView, requestHandler);
                        case ActionSelect -> new ActionSelectView(this::changeView, requestHandler);
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
}
