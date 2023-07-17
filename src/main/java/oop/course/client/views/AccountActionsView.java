package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.DeactivateAccountRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.DeactivateAccountResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AccountActionsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String account;

    public AccountActionsView(Consumer<IView> changeViewHandler, Runnable onExit, Function<Request, BasicResponse> requestHandler,
                              String token, String accountNumber) {
        this.onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.account = accountNumber;
        this.onExit = onExit;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Action selector");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please select an action.").attachTo(contentPanel);

        new TerminalButton("Money transfer", () -> {
            window.close();
            onChangeView.accept(new TransferView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        var accountNumber = new TerminalFormKeyValuePair("accountNumber", new TerminalInputPair(new TerminalText(
                "Account Number"), new TerminalImmutableTextBox(account)));
        var accountDeactivationForm = new TerminalForm(List.of(accountNumber));

        new TerminalButton("Request a statement", () -> {
            window.close();
            onChangeView.accept(new StatementInputView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("View transaction history", () -> {
            window.close();
            onChangeView.accept(new TransactionsView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("Create a request", () -> {
            window.close();
            onChangeView.accept(new CreateRequestView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("Set up an autopayment", () -> {
            window.close();
            onChangeView.accept(new CreateAutoPaymentView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("Deactivate an account", () -> {
            Request deactivateRequest = new DeactivateAccountRequest(token, accountDeactivationForm);
            var response = new DeactivateAccountResponse(requestHandler.apply(deactivateRequest));
            if (response.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Account successfully deactivated",
                        MessageDialogButton.Continue);
                window.close();
                onChangeView.accept(new AccountsView(onChangeView, onExit, requestHandler, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Account could not be deactivated",
                        MessageDialogButton.Abort);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Cancel", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, requestHandler, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
