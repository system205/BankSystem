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

public final class AccountActionsView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String account;

    public AccountActionsView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
                              String token, String accountNumber) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.account = accountNumber;
        this.exitAction = exitAction;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var accountDeactivationForm = new TerminalForm(List.of(new TerminalFormKeyValuePair("accountNumber",
            new TerminalInputPair(new TerminalText("Account Number"), new TerminalFixedTextBox(account)))));

        var window = new TerminalWindow(
            "Action selector",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            new TerminalText("Please select an action."),
            new TerminalButton("Money transfer", this::onMoneyTransfer),
            new TerminalButton("Request a statement", this::onStatementRequest),
            new TerminalButton("View transaction history", this::onTransactionHistory),
            new TerminalButton("Create a request", this::onRequestCreate),
            new TerminalButton("Set up an auto-payment", this::onAutoPaymentCreate),
            new TerminalButton("List/cancel auto-payments", this::onAutoPaymentList),
            new TerminalButton("Deactivate an account", () -> onDeactivate(gui, accountDeactivationForm)),
            new TerminalButton("Cancel", this::onCancel)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onMoneyTransfer() {
        changeView.accept(new TransferView(changeView, exitAction, serverBridge, token, account));
    }

    private void onStatementRequest() {
        changeView.accept(new StatementInputView(changeView, exitAction, serverBridge, token, account));
    }

    private void onTransactionHistory() {
        changeView.accept(new TransactionsView(changeView, exitAction, serverBridge, token, account));
    }

    private void onRequestCreate() {
        changeView.accept(new CreateRequestView(changeView, exitAction, serverBridge, token, account));
    }

    private void onAutoPaymentCreate() {
        changeView.accept(new CreateAutoPaymentView(changeView, exitAction, serverBridge, token, account));
    }

    private void onAutoPaymentList() {
        changeView.accept(new ListAutoPaymentsView(changeView, exitAction, serverBridge, token, account));
    }

    private void onDeactivate(WindowBasedTextGUI gui, TerminalForm accountDeactivationForm) {
        var response = serverBridge.execute(new DeactivateAccountRequest(token, accountDeactivationForm.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.Continue);
            changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Abort);
        }
    }

    private void onCancel() {
        changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
    }
}
