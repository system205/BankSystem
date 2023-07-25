package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.RegisterRequest;

import java.util.List;
import java.util.function.Consumer;

public final class RegisterView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;

    public RegisterView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge) {
        this.changeView = changeView;
        this.exitAction = exitAction;
        this.serverBridge = serverBridge;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("BankSystem registration", new Panel(new LinearLayout(Direction.VERTICAL)));
        new TerminalText("Please enter your data for registration.").attachTo(window.panel());
        var form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "email",
                                new TerminalInputPair(
                                        new TerminalText("Email"),
                                        new TerminalTextBox()
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "name",
                                new TerminalInputPair(
                                        new TerminalText("Name"),
                                        new TerminalTextBox()
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "surname",
                                new TerminalInputPair(
                                        new TerminalText("Surname"),
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
        new TerminalButton("Register", () -> onRegister(gui, form)).attachTo(window.panel());
        new TerminalButton("Back to login page", this::onReturn).attachTo(window.panel());
        new TerminalButton("Exit", this::onExit);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onExit() {
        exitAction.run();
    }

    private void onReturn() {
        changeView.accept(new LoginView(changeView, exitAction, serverBridge));
    }

    private void onRegister(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = serverBridge.execute(new RegisterRequest(form.json()));
        MessageDialog.showMessageDialog(gui, "Result", resp.message(), MessageDialogButton.OK);
        if (resp.isSuccess()) {
            changeView.accept(new LoginView(changeView, exitAction, serverBridge));
        }
    }
}
