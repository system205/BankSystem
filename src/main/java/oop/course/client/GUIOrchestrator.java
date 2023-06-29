package oop.course.client;

import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

public class GUIOrchestrator {
    private IView.Type currentView;
    private boolean changePending;
    public GUIOrchestrator() {
        currentView = IView.Type.Login;
        changePending = false;
    }

    public void mainLoop(Screen screen)
    {
        try {
            currentView = IView.Type.Login;
            while (true) {
                if (changePending)
                {
                    changePending = false;
                    if (currentView == IView.Type.None)
                    {
                        screen.stopScreen();
                        break;
                    }
                    IView view = switch (currentView) {
                        case Account -> new AccountView(screen);
                        case Transfer -> new TransferView(screen);
                        case Login -> new LoginView(screen);
                        case Register -> new RegisterView(screen);
                        case None -> throw new RuntimeException();
                    };
                    view.registerChangeViewHandler(this::changeView);
                    view.show();
                }

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
