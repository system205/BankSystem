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

    public RegisterView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge) {
        onChangeView = changeViewHandler;
        this.onExit = onExit;
        this.serverBridge = serverBridge;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem registration");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);
        var email = new TerminalFormKeyValuePair("email", new TerminalInputPair(new TerminalText("Email"),
                new TerminalTextBox()));
        var name = new TerminalFormKeyValuePair("name", new TerminalInputPair(new TerminalText("Name"),
                new TerminalTextBox()));
        var surname = new TerminalFormKeyValuePair("surname", new TerminalInputPair(new TerminalText("Surname"),
                new TerminalTextBox()));
        var passwordField1 = new TerminalPasswordBox();
        var passwordField2 = new TerminalPasswordBox();
        var password = new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"),
                passwordField1));
        var password2 = new TerminalInputPair(new TerminalText("Repeat password"), passwordField2);

        var form = new TerminalForm(List.of(email, name, surname, password));

        email.attachTo(contentPanel);
        name.attachTo(contentPanel);
        surname.attachTo(contentPanel);
        password.attachTo(contentPanel);
        password2.attachTo(contentPanel);

        new TerminalButton("Register", () -> {
            if (!passwordField1.text().equals(passwordField2.text())) {
                MessageDialog.showMessageDialog(gui, "Error", "Passwords do not match", MessageDialogButton.Close);
                return;
            }
            //Check all other requirements for the fields
            var resp = serverBridge.execute(new RegisterRequest(form));
            if (resp.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Result", "Account successfully created! You may now log in",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new LoginView(onChangeView, onExit, serverBridge));
            } else {
                MessageDialog.showMessageDialog(gui, "Result", "Something went terribly wrong!",
                        MessageDialogButton.Abort);
            }
        }).attachTo(contentPanel);
        new TerminalButton("Back to login page", () -> {
            window.close();
            onChangeView.accept(new LoginView(onChangeView, onExit, serverBridge));
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onExit.run();
        });

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
