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
    private final TerminalWindow window;
    private final Panel contentPanel;
    private final String token;
    private final String account;
    private final TerminalForm accountDeactivationForm;

    public AccountActionsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                              String token, String accountNumber) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.account = accountNumber;
        this.onExit = onExit;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Action selector", contentPanel);
        this.accountDeactivationForm = new TerminalForm(List.of(new TerminalFormKeyValuePair("accountNumber",
                new TerminalInputPair(new TerminalText("Account Number"), new TerminalImmutableTextBox(account)))));
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        new TerminalText("Please select an action.").attachTo(contentPanel);

        new TerminalButton("Money transfer", this::onMoneyTransfer).attachTo(contentPanel);

        new TerminalButton("Request a statement", this::onStatementRequest).attachTo(contentPanel);
        new TerminalButton("View transaction history", this::onTransactionHistory).attachTo(contentPanel);
        new TerminalButton("Create a request", this::onRequestCreate).attachTo(contentPanel);
        new TerminalButton("Set up an auto-payment", this::onAutoPaymentCreate).attachTo(contentPanel);
        new TerminalButton("List/cancel auto-payments", this::onAutoPaymentList).attachTo(contentPanel);
        new TerminalButton("Deactivate an account", () -> onDeactivate(gui)).attachTo(contentPanel);

        new TerminalButton("Cancel", this::onCancel).attachTo(contentPanel);

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onMoneyTransfer() {
        onChangeView.accept(new TransferView(onChangeView, onExit, serverBridge, token, account));
    }

    private void onStatementRequest() {
        onChangeView.accept(new StatementInputView(onChangeView, onExit, serverBridge, token, account));
    }

    private void onTransactionHistory() {
        onChangeView.accept(new TransactionsView(onChangeView, onExit, serverBridge, token, account));
    }

    private void onRequestCreate() {
        onChangeView.accept(new CreateRequestView(onChangeView, onExit, serverBridge, token, account));
    }

    private void onAutoPaymentCreate() {
        onChangeView.accept(new CreateAutoPaymentView(onChangeView, onExit, serverBridge, token, account));
    }

    private void onAutoPaymentList() {
        onChangeView.accept(new ListAutoPaymentsView(onChangeView, onExit, serverBridge, token, account));
    }

    private void onDeactivate(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new DeactivateAccountRequest(token, accountDeactivationForm.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.Continue);
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Abort);
        }
    }

    private void onCancel() {
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }
}
