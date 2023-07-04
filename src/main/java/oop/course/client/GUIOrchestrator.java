package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.Objects;

public class GUIOrchestrator {
    private IView.Type currentView;
    private final WindowBasedTextGUI textGUI;
    private String token;
    private boolean changePending;

    public GUIOrchestrator(Screen screen) {
        currentView = IView.Type.Login;
        changePending = true;
        textGUI = new MultiWindowTextGUI(screen);
        token = "";
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
                        case Account -> new AccountView(token);
                        case Transfer -> new TransferView();
                        case Login -> new LoginView();
                        case Register -> new RegisterView();
                        case ActionSelect -> new ActionSelectView();
                        case None -> throw new RuntimeException();
                    };
                    view.registerChangeViewHandler(this::changeView);
                    view.show(textGUI);
                }
                textGUI.getGUIThread().processEventsAndUpdate();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    private void changeView(IView.Type type, String string) {
        changePending = true;
        currentView = type;
        if (!Objects.equals(string, "")) {
            token = string;
        }
    }
}
