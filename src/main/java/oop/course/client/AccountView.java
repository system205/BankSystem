package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class AccountView implements View {
    private final Screen screen;
    private Type nextView;

    public AccountView(Terminal terminal) throws IOException {
        screen = new TerminalScreen(terminal);
        nextView = Type.None;
    }

    @Override
    public Type show() throws IOException {
        screen.startScreen();
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("Account");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        contentPanel.addComponent(new Label("Welcome to your account."));
        contentPanel.addComponent(new Label("Name:"));
        contentPanel.addComponent(new Label("Ivan Ivanov"));
        contentPanel.addComponent(new Label("Account number:"));
        contentPanel.addComponent(new Label("123456"));
        contentPanel.addComponent(new Label("Balance"));
        contentPanel.addComponent(new Label("120.0$"));

        contentPanel.addComponent(new Button("Transfer money", () -> {
            nextView = Type.Transfer;
            window.close();
        }));

        contentPanel.addComponent(new Button("Logout", () -> {
            nextView = Type.Login;
            window.close();
        }));

        contentPanel.addComponent(new Button("Logout & exit", () -> {
            nextView = Type.None;
            window.close();
        }));

        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
        return nextView;
    }
}
