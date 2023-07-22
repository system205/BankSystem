package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.LoginRequest;

import java.util.List;
import java.util.function.Consumer;

public class LoginView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;

    public LoginView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge) {
        onChangeView = changeViewHandler;
        this.onExit = onExit;
        this.serverBridge = serverBridge;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem authentication");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalText("Welcome to the BankSystem client application!\nPlease, register or login into your " +
                "existing" + " account.").attachTo(contentPanel);

        var email = new TerminalTextBox();
        var username = new TerminalFormKeyValuePair("email", new TerminalInputPair(new TerminalText("Email"), email));
        var password = new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"),
                new TerminalPasswordBox()));

        var form = new TerminalForm(List.of(username, password));

        username.attachTo(contentPanel);
        password.attachTo(contentPanel);

        new TerminalButton("Login", () -> {
            var resp = serverBridge.execute(new LoginRequest(form));
            if (resp.isWrongCredentials()) {
                MessageDialog.showMessageDialog(gui, "Authentication error", "Wrong credentials",
                        MessageDialogButton.Close);
            } else if (resp.isSuccess()) {
                window.close();
                onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, resp.token()));
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Unexpected error", MessageDialogButton.Close);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Register page", () -> {
            window.close();
            onChangeView.accept(new RegisterView(onChangeView, onExit, serverBridge));
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onExit.run();
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
