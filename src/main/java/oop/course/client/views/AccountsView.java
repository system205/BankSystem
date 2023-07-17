package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalModernButton;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.AccountsRequest;
import oop.course.client.requests.BecomeManagerRequest;
import oop.course.client.requests.NewAccountRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.AccountsResponse;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.BecomeManagerResponse;
import oop.course.client.responses.NewAccountResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public class AccountsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;

    public AccountsView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler,
                        String token) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Account selection");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Request req = new AccountsRequest(token);
        var resp = new AccountsResponse(requestHandler.apply(req));
        var accounts = resp.accounts();

        for (var account : accounts) {
            var t = "Account number: " + account.accountNumber() + " " + "Balance: " + account.balance();
            new TerminalModernButton(t, () -> {
                window.close();
                onChangeView.accept(new AccountActionsView(onChangeView, requestHandler, token,
                        account.accountNumber()));
            }).attachTo(contentPanel);
        }

        new TerminalButton("Create an account", () -> {
            Request newAccountRequest = new NewAccountRequest(token);
            var newAccountResponse = new NewAccountResponse(requestHandler.apply(newAccountRequest));
            if (newAccountResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success",
                        "Successfully created an account with number " + newAccountResponse.accountNumber() + " with " +
                                "the starting balance " + newAccountResponse.accountBalance());
                window.close();
                onChangeView.accept(new AccountsView(onChangeView, requestHandler, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Error", "Unexpected error has occurred",
                        MessageDialogButton.Close);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Check my requests", () -> {
            window.close();
            onChangeView.accept(new CheckRequestsView(onChangeView, requestHandler, token));
        }).attachTo(contentPanel);

        new TerminalButton("Request a manager status", () -> {
            var request = new BecomeManagerRequest(token);
            var response = new BecomeManagerResponse(requestHandler.apply(request));
            if (response.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "The request has been sent successfully. Assigned id:" +
                        " " + response.id(), MessageDialogButton.Continue);
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "The request could not be sent or you already applied" +
                        " to the job", MessageDialogButton.Close);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Admin actions", () -> {
            window.close();
            onChangeView.accept(new AdminActionsView(onChangeView, requestHandler, token));
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
