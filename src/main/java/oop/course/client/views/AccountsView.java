package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.TerminalAccountsTable;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.AccountsRequest;
import oop.course.client.requests.BecomeManagerRequest;
import oop.course.client.requests.NewAccountRequest;

import java.util.List;
import java.util.function.Consumer;

public class AccountsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public AccountsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge, String token) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Account selection", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var resp = serverBridge.execute(new AccountsRequest(token));
        resp.fillAccountsTable((List<List<String>> rows) -> new TerminalAccountsTable(rows, this::onAccountSelected)).attachTo(contentPanel);
        new TerminalButton("Create an account", () -> onCreateAccount(gui)).attachTo(contentPanel);
        new TerminalButton("Check my requests", this::onCheckRequests).attachTo(contentPanel);
        new TerminalButton("Request a manager status", () -> onRequestManager(gui)).attachTo(contentPanel);
        new TerminalButton("Admin actions", this::onAdminActions).attachTo(contentPanel);
        new TerminalButton("Logout", this::onLogout).attachTo(contentPanel);
        new TerminalButton("Logout & exit", this::onExit).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onAccountSelected(List<String> row) {
        onChangeView.accept(new AccountActionsView(onChangeView, onExit, serverBridge, token, row.get(0)));
    }

    private void onCreateAccount(WindowBasedTextGUI gui) {
        var newAccountResponse = serverBridge.execute(new NewAccountRequest(token));
        if (newAccountResponse.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success",
                    "Successfully created an account with number " + newAccountResponse.accountNumber() + " with " +
                            "the starting balance " + newAccountResponse.accountBalance());
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Error", "Unexpected error has occurred", MessageDialogButton.Close);
        }
    }

    private void onCheckRequests() {
        onChangeView.accept(new CheckRequestsView(onChangeView, onExit, serverBridge, token));
    }

    private void onRequestManager(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new BecomeManagerRequest(token));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", "The request has been sent successfully. Assigned " + "id"
                    + ":" + " " + response.id(), MessageDialogButton.Continue);
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", "The request could not be sent or you already " +
                    "applied" + " to the job", MessageDialogButton.Close);
        }
    }

    private void onAdminActions() {
        onChangeView.accept(new AdminActionsView(onChangeView, onExit, serverBridge, token));
    }

    private void onLogout() {
        onChangeView.accept(new LoginView(onChangeView, onExit, serverBridge));
    }

    private void onExit() {
        onExit.run();
    }
}
