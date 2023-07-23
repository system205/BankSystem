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

public class RegisterView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public RegisterView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge) {
        onChangeView = changeViewHandler;
        this.onExit = onExit;
        this.serverBridge = serverBridge;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("BankSystem registration", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);

        var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("email",
                new TerminalInputPair(new TerminalText("Email"), new TerminalTextBox())),
                new TerminalFormKeyValuePair("name", new TerminalInputPair(new TerminalText("Name"),
                        new TerminalTextBox())), new TerminalFormKeyValuePair("surname",
                        new TerminalInputPair(new TerminalText("Surname"), new TerminalTextBox())),
                new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"),
                        new TerminalPasswordBox()))));

        form.attachTo(contentPanel);
        new TerminalButton("Register", () -> onRegister(gui, form)).attachTo(contentPanel);
        new TerminalButton("Back to login page", this::onReturn).attachTo(contentPanel);
        new TerminalButton("Exit", this::onExit);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onExit() {
        onExit.run();
    }

    private void onReturn() {
        onChangeView.accept(new LoginView(onChangeView, onExit, serverBridge));
    }

    private void onRegister(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = serverBridge.execute(new RegisterRequest(form.json()));
        MessageDialog.showMessageDialog(gui, "Result", resp.message(), MessageDialogButton.OK);
        if (resp.isSuccess()) {
            onChangeView.accept(new LoginView(onChangeView, onExit, serverBridge));
        }
    }
}
