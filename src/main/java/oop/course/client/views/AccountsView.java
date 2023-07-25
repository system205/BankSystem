package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

public final class AccountsView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;

    public AccountsView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge, String token) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var resp = serverBridge.execute(new AccountsRequest(token));
        TerminalGUIElement accounts;
        if (resp.isSuccess()) {
            accounts = new TerminalAccountsTable(resp.accounts(), this::onAccountSelected);
        } else {
            accounts = new TerminalText(resp.message());
        }
        var window = new TerminalWindow(
            "Account selection",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            accounts,
            new TerminalButton("Create an account", () -> onCreateAccount(gui)),
            new TerminalButton("Check my requests", this::onCheckRequests),
            new TerminalButton("Request a manager status", () -> onRequestManager(gui)),
            new TerminalButton("Admin actions", this::onAdminActions),
            new TerminalButton("Logout", this::onLogout),
            new TerminalButton("Logout & exit", this::onExit)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onAccountSelected(List<String> row) {
        changeView.accept(new AccountActionsView(changeView, exitAction, serverBridge, token, row.get(0)));
    }

    private void onCreateAccount(WindowBasedTextGUI gui) {
        var newAccountResponse = serverBridge.execute(new NewAccountRequest(token));
        if (newAccountResponse.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", newAccountResponse.message(), MessageDialogButton.OK);
            changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Error", newAccountResponse.message(), MessageDialogButton.Close);
        }
    }

    private void onCheckRequests() {
        changeView.accept(new CheckRequestsView(changeView, exitAction, serverBridge, token));
    }

    private void onRequestManager(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new BecomeManagerRequest(token));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", "Successfully posted a job offer", MessageDialogButton.OK);
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }

    private void onAdminActions() {
        changeView.accept(new AdminActionsView(changeView, exitAction, serverBridge, token));
    }

    private void onLogout() {
        changeView.accept(new LoginView(changeView, exitAction, serverBridge));
    }

    private void onExit() {
        exitAction.run();
    }
}
