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

public final class LoginView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;

    public LoginView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge) {
        this.changeView = changeView;
        this.exitAction = exitAction;
        this.serverBridge = serverBridge;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("BankSystem authentication", new Panel(new LinearLayout(Direction.VERTICAL)));

        new TerminalText("Welcome to the BankSystem client application!\n" + "Please, register or login into your " +
                "existing account.").attachTo(window.panel());

        TerminalForm form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "email",
                                new TerminalInputPair(
                                        new TerminalText("Email"),
                                        new TerminalTextBox()
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "password",
                                new TerminalInputPair(
                                        new TerminalText("Password"),
                                        new TerminalPasswordBox()
                                )
                        )
                )
        );

        form.attachTo(window.panel());

        new TerminalButton("Login", () -> onLogin(gui, form)).attachTo(window.panel());
        new TerminalButton("Register page", this::onRegister).attachTo(window.panel());
        new TerminalButton("Exit", this::onExit).attachTo(window.panel());

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onLogin(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = this.serverBridge.execute(new LoginRequest(form.json()));

        if (resp.isSuccess()) {
            this.changeView.accept(new AccountsView(this.changeView, this.exitAction, this.serverBridge, resp.token()));
        } else {
            MessageDialog.showMessageDialog(gui, "Authentication error", resp.message(), MessageDialogButton.Close);
        }
    }

    private void onRegister() {
        this.changeView.accept(new RegisterView(this.changeView, this.exitAction, this.serverBridge));
    }

    private void onExit() {
        this.exitAction.run();
    }
}
