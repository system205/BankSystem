package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.LoginRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.LoginResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class LoginView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;

    public LoginView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem authentication");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalText("Welcome to the BankSystem client application!\nPlease, register or login into your existing" +
                " account.").attachTo(contentPanel);

        var email = new TerminalTextBox();
        var username = new TerminalFormKeyValuePair("email", new TerminalInputPair(new TerminalText("Email"), email));
        var password = new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"),
                new TerminalPasswordBox()));

        var form = new TerminalForm(List.of(username, password));

        username.attachTo(contentPanel);
        password.attachTo(contentPanel);

        new TerminalButton("Login", () -> {
            Request req = new LoginRequest(form);
            var resp = new LoginResponse(requestHandler.apply(req));
            if (resp.isWrongCredentials()) {
                MessageDialog.showMessageDialog(gui, "Authentication error", "Wrong credentials",
                        MessageDialogButton.Close);
            } else if (resp.isSuccess()) {
                window.close();
                onChangeView.accept(new AccountsView(onChangeView, requestHandler, resp.token()));
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Unexpected error", MessageDialogButton.Close);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Register page", () -> {
            window.close();
            onChangeView.accept(new RegisterView(onChangeView, requestHandler));
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onChangeView.accept(null);
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
