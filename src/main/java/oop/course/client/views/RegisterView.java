package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.RegisterRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.RegisterResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class RegisterView implements IView {
    private final Consumer<Type> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;

    public RegisterView(Consumer<Type> changeViewHandler, Function<Request, BasicResponse> requestHandler) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem registration");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);
        var email = new TerminalFormKeyValuePair("email", new TerminalInputPair(new TerminalText("Email"), new TerminalTextBox()));
        var name = new TerminalFormKeyValuePair("name", new TerminalInputPair(new TerminalText("Name"), new TerminalTextBox()));
        var surname = new TerminalFormKeyValuePair("surname", new TerminalInputPair(new TerminalText("Surname"), new TerminalTextBox()));
        var passwordField1 = new TerminalPasswordBox();
        var passwordField2 = new TerminalPasswordBox();
        var password = new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"), passwordField1));
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
            Request req = new RegisterRequest(form);
            var resp = new RegisterResponse(requestHandler.apply(req));
            MessageDialog.showMessageDialog(gui, "Result", String.valueOf(resp.isSuccess()), MessageDialogButton.OK);
        }
        ).attachTo(contentPanel);
        new TerminalButton("Back to login page", () -> {
            window.close();
            onChangeView.accept(Type.Login);
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onChangeView.accept(Type.None);
        });

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
