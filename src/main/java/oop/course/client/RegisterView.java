package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class RegisterView implements View {
    private final Screen screen;
    private Type nextView;


    public RegisterView(Terminal terminal) throws IOException {
        screen = new TerminalScreen(terminal);
        nextView = Type.None;
    }

    @Override
    public Type show() throws IOException {
        screen.startScreen();
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("BankSystem registration");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);
        new TerminalText("Username").attachTo(contentPanel);
        new TerminalTextBox().attachTo(contentPanel);
        new TerminalText("Password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);
        new TerminalText("Repeat password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);
        new TerminalButton("Register", () ->
                MessageDialog.showMessageDialog(textGUI, "Success", "You may now login into your account.", MessageDialogButton.OK)
        ).attachTo(contentPanel);
        new TerminalButton("Back to login page", () -> {
            nextView = Type.Login;
            window.close();
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            nextView = Type.None;
            window.close();
        });

        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
        return nextView;
    }
}
