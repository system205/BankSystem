package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

public final class AdminRequestsView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;

    public AdminRequestsView(Consumer<IView> changeViewHandler, Runnable exitAction, ServerBridge serverBridge,
                             String token) {
        this.changeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new ManagerRequestsRequest(token));
        TerminalGUIElement requests;
        if (response.isSuccess()) {
            requests = new TerminalBankRequestTable(response.requests(), row -> onRowSelected(row, gui));
        } else {
            requests = new TerminalText(response.message());
        }
        var window = new TerminalWindow(
            "Admin requests panel",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            requests,
            new TerminalButton("Return", this::onReturn)
        );
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        changeView.accept(new AdminActionsView(changeView, exitAction, serverBridge, token));
    }

    private void onRowSelected(List<String> row, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui,
            "Select an action", "Do you want to approve the request?",
            MessageDialogButton.Yes, MessageDialogButton.No, MessageDialogButton.Cancel);
        TerminalForm form;
        if (res == MessageDialogButton.Yes) {
            form = new TerminalForm(
                List.of(
                    new TerminalFormKeyValuePair(
                        "id",
                        new TerminalInputPair(
                            new TerminalText("Request id"),
                            new TerminalFixedTextBox(row.get(0)))
                    ),
                    new TerminalFormKeyValuePair(
                        "status",
                        new TerminalInputPair(
                            new TerminalText("Status"),
                            new TerminalFixedTextBox("approved"))
                    )
                )
            );
        } else if (res == MessageDialogButton.No) {
            form = new TerminalForm(
                List.of(
                    new TerminalFormKeyValuePair(
                        "id",
                        new TerminalInputPair(
                            new TerminalText("Request id"),
                            new TerminalFixedTextBox(row.get(0)))
                    ),
                    new TerminalFormKeyValuePair(
                        "status",
                        new TerminalInputPair(
                            new TerminalText("Status"),
                            new TerminalFixedTextBox("denied"))
                    )
                )
            );
        } else {
            return;
        }
        var response = serverBridge.execute(new HandleRequestRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            changeView.accept(new AdminRequestsView(changeView, exitAction, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }
}
