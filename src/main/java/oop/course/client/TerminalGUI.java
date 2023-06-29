package oop.course.client;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class TerminalGUI implements GUI {
    private final Terminal terminal;
    private final Screen screen;
    private View.Type currentView;

    public TerminalGUI() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
        screen = new TerminalScreen(terminal);
    }

    @Override
    public void startLooping() {
        try {
            screen.startScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            currentView = View.Type.Login;
            while (true) {
                boolean close = false;
                switch (currentView) {
                    case Account -> new AccountView(screen).registerChangeViewHandler(this::changeView);
                    case Transfer -> new TransferView(screen).registerChangeViewHandler(this::changeView);
                    case None -> {
                        screen.stopScreen();
                        terminal.close();
                        close = true;
                    }
                    case Login -> new LoginView(screen).registerChangeViewHandler(this::changeView);
                    case Register -> new RegisterView(screen).registerChangeViewHandler(this::changeView);
                }
                if (close) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
        try {
            screen.stopScreen();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeView(View.Type type) {
        currentView = type;
    }
}
