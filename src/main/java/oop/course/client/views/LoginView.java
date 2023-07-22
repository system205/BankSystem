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
    private final TerminalWindow window;
    private final Panel contentPanel;

    public LoginView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge) {
        this.onChangeView = changeViewHandler;
        this.onExit = onExit;
        this.serverBridge = serverBridge;
        this.contentPanel = new Panel(
                new LinearLayout(
                        Direction.VERTICAL
                )
        );
        this.window = new TerminalWindow("BankSystem authentication", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        new TerminalText("Welcome to the BankSystem client application!\nPlease, register or login into your " +
                "existing account.").attachTo(contentPanel);
        var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("email",
                        new TerminalInputPair(new TerminalText("Email"), new TerminalTextBox())),
                new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"),
                        new TerminalPasswordBox()))));
        form.attachTo(contentPanel);
        new TerminalButton("Login", () -> onLogin(gui, form)).attachTo(contentPanel);
        new TerminalButton("Register page", this::onRegister).attachTo(contentPanel);
        new TerminalButton("Exit", this::onExit).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    private void onLogin(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = serverBridge.execute(new LoginRequest(form));
        if (!resp.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Authentication error", resp.message(), MessageDialogButton.Close);
        } else {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, resp.token()));
        }
    }

    private void onRegister() {
        window.close();
        onChangeView.accept(new RegisterView(onChangeView, onExit, serverBridge));
    }

    private void onExit() {
        window.close();
        onExit.run();
    }
}
