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
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.AccountsRequest;
import oop.course.client.requests.BecomeManagerRequest;
import oop.course.client.requests.NewAccountRequest;

import java.util.List;
import java.util.function.Consumer;

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
        var window = new TerminalWindow("Account selection", new Panel(new LinearLayout(Direction.VERTICAL)));

        var resp = serverBridge.execute(new AccountsRequest(token));
        if (resp.isSuccess()) {
            new TerminalAccountsTable(resp.accounts(), this::onAccountSelected).attachTo(window.panel());
        } else {
            new TerminalText(resp.message()).attachTo(window.panel());
        }
        new TerminalButton("Create an account", () -> onCreateAccount(gui)).attachTo(window.panel());
        new TerminalButton("Check my requests", this::onCheckRequests).attachTo(window.panel());
        new TerminalButton("Request a manager status", () -> onRequestManager(gui)).attachTo(window.panel());
        new TerminalButton("Admin actions", this::onAdminActions).attachTo(window.panel());
        new TerminalButton("Logout", this::onLogout).attachTo(window.panel());
        new TerminalButton("Logout & exit", this::onExit).attachTo(window.panel());
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
