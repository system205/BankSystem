package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.DeactivateAccountRequest;

import java.util.List;
import java.util.function.Consumer;

public class AccountActionsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final String account;

    public AccountActionsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                              String token, String accountNumber) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
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
            onChangeView.accept(new TransferView(onChangeView, onExit, serverBridge, token, account));
        }).attachTo(contentPanel);

        var accountNumber = new TerminalFormKeyValuePair("accountNumber", new TerminalInputPair(new TerminalText(
                "Account Number"), new TerminalImmutableTextBox(account)));
        var accountDeactivationForm = new TerminalForm(List.of(accountNumber));

        new TerminalButton("Request a statement", () -> {
            window.close();
            onChangeView.accept(new StatementInputView(onChangeView, onExit, serverBridge, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("View transaction history", () -> {
            window.close();
            onChangeView.accept(new TransactionsView(onChangeView, onExit, serverBridge, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("Create a request", () -> {
            window.close();
            onChangeView.accept(new CreateRequestView(onChangeView, onExit, serverBridge, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("Set up an auto-payment", () -> {
            window.close();
            onChangeView.accept(new CreateAutoPaymentView(onChangeView, onExit, serverBridge, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("List/cancel auto-payments", () -> {
            window.close();
            onChangeView.accept(new ListAutoPaymentsView(onChangeView, onExit, serverBridge, token, account));
        }).attachTo(contentPanel);

        new TerminalButton("Deactivate an account", () -> {
            var response = serverBridge.execute(new DeactivateAccountRequest(token, accountDeactivationForm));
            if (response.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Account successfully deactivated",
                        MessageDialogButton.Continue);
                window.close();
                onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Account could not be deactivated",
                        MessageDialogButton.Abort);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Cancel", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
