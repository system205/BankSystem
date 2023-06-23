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
    private ViewType nextView;


    public RegisterView(Terminal terminal) throws IOException {
        screen = new TerminalScreen(terminal);
        nextView = ViewType.NoView;
    }

    @Override
    public ViewType show() throws IOException {
        screen.startScreen();
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("BankSystem registration");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        contentPanel.addComponent(new Label("Please enter your data for registration."));
        contentPanel.addComponent(new Label("Username"));
        contentPanel.addComponent(new TextBox());
        contentPanel.addComponent(new Label("Password"));
        contentPanel.addComponent(new TextBox().setMask('*'));
        contentPanel.addComponent(new Label("Repeat password"));
        contentPanel.addComponent(new TextBox().setMask('*'));

        contentPanel.addComponent(new Button("Register", () -> MessageDialog.showMessageDialog(textGUI, "Success", "You may now login into your account.", MessageDialogButton.OK)));

        contentPanel.addComponent(new Button("Back to login page", () -> {
            nextView = ViewType.LoginView;
            window.close();
        }));

        contentPanel.addComponent(new Button("Exit", () -> {
            nextView = ViewType.NoView;
            window.close();
        }));

        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
        return nextView;
    }
}
