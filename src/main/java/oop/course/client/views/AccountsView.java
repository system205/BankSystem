package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.AccountsRequest;
import oop.course.client.requests.NewAccountRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.AccountsResponse;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.NewAccountResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public class AccountsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String email;

    public AccountsView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token, String email) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.email = email;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Account selection");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Request req = new AccountsRequest(token);
        var resp = new AccountsResponse(requestHandler.apply(req));
        var accounts = resp.accounts();

        for (var account : accounts) {
            var t = "Account number: " + account.accountNumber() + " " +
                    "Balance: " + account.balance();
            new TerminalButton(t, () -> {
                window.close();
                onChangeView.accept(new TransferView(onChangeView, requestHandler, token, email));
            }, true).attachTo(contentPanel);
        }

        new TerminalButton("Create an account", () -> {
            Request newAccountRequest = new NewAccountRequest(token);
            var newAccountResponse = new NewAccountResponse(requestHandler.apply(newAccountRequest));
            if (newAccountResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully created an account with number " +
                        newAccountResponse.accountNumber() + " with the starting balance " + newAccountResponse.accountBalance());
                window.close();
                onChangeView.accept(new AccountsView(onChangeView, requestHandler, token, email));
            }
            else {
                MessageDialog.showMessageDialog(gui, "Error", "Unexpected error has occurred", MessageDialogButton.Close);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Logout", () -> {
            window.close();
            onChangeView.accept(new LoginView(onChangeView, requestHandler));
        }).attachTo(contentPanel);

        new TerminalButton("Logout & exit", () -> {
            window.close();
            onChangeView.accept(null);
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
