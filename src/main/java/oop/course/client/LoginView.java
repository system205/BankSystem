package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class LoginView implements View {
    private final Screen screen;
    private Type nextView;

    public LoginView(Terminal terminal) throws IOException {
        screen = new TerminalScreen(terminal);
        nextView = Type.None;
    }

    @Override
    public Type show() throws IOException {
        screen.startScreen();
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("BankSystem authentication");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Label welcomeText = new Label("Welcome to the BankSystem client application!\nPlease, register or login into your existing account.");
        contentPanel.addComponent(welcomeText);
        contentPanel.addComponent(new Label("Username"));
        contentPanel.addComponent(new TextBox());
        contentPanel.addComponent(new Label("Password"));
        contentPanel.addComponent(new TextBox().setMask('*'));

        contentPanel.addComponent(new Button("Login", () -> {
            nextView = Type.Account;
            window.close();
        }));
        contentPanel.addComponent(new Button("Register page", () -> {
            nextView = Type.Register;
            window.close();
        }));
        contentPanel.addComponent(new Button("Exit", () -> {
            nextView = Type.None;
            window.close();
        }));
        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
        return nextView;
    }
}
