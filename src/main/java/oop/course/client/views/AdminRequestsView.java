package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.HandleRequestRequest;
import oop.course.client.requests.ManagerRequestsRequest;

import java.util.List;
import java.util.function.Consumer;

public final class AdminRequestsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public AdminRequestsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                             String token) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Admin requests panel", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new ManagerRequestsRequest(token));

        if (response.isSuccess()) {
            response.fillRequestsTable(
                    (List<List<String>> rows) -> new TerminalBankRequestTable(rows, (List<String> row) -> onRowSelected(row, gui))
            ).attachTo(contentPanel);
        } else {
            new TerminalText(response.message()).attachTo(contentPanel);
        }

        new TerminalButton("Return", this::onReturn).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        onChangeView.accept(new AdminActionsView(onChangeView, onExit, serverBridge, token));
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
                                            new TerminalImmutableTextBox(row.get(0)))
                            ),
                            new TerminalFormKeyValuePair(
                                    "status",
                                    new TerminalInputPair(
                                            new TerminalText("Status"),
                                            new TerminalImmutableTextBox("approved"))
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
                                            new TerminalImmutableTextBox(row.get(0)))
                            ),
                            new TerminalFormKeyValuePair(
                                    "status",
                                    new TerminalInputPair(
                                            new TerminalText("Status"),
                                            new TerminalImmutableTextBox("denied"))
                            )
                    )
            );
        } else {
            return;
        }
        var response = serverBridge.execute(new HandleRequestRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            onChangeView.accept(new AdminRequestsView(onChangeView, onExit, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }
}
