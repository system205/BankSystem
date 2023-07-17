package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.BankRequest;
import oop.course.client.gui.*;
import oop.course.client.requests.HandleRequestRequest;
import oop.course.client.requests.ManagerRequestsRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.HandleRequestResponse;
import oop.course.client.responses.ManagerRequestsResponse;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdminRequestsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final TerminalWindow window;

    public AdminRequestsView(Consumer<IView> changeViewHandler, Runnable onExit, Function<Request, BasicResponse> requestHandler,
                             String token) {
        this.onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.onExit = onExit;
        this.window = new TerminalWindow("Account selection");
    }


    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        var request = new ManagerRequestsRequest(token);
        var response = new ManagerRequestsResponse(requestHandler.apply(request));

        if (response.isSuccess()) {
            List<BankRequest> requests = response.requests();
            new TerminalBankRequestTable(requests, (List<String> row) -> onRowSelected(row, gui)).attachTo(contentPanel);
        } else {
            new TerminalText("Could not fetch data from the server").attachTo(contentPanel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AdminActionsView(onChangeView, onExit, requestHandler, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    private void onRowSelected(List<String> row, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui, "Select an action", "Do you want to approve the " +
                "request?", MessageDialogButton.Yes, MessageDialogButton.No, MessageDialogButton.Cancel);
        if (res == MessageDialogButton.Yes) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("id",
                    new TerminalInputPair(new TerminalText("Request id"),
                            new TerminalImmutableTextBox(row.get(0)))), new TerminalFormKeyValuePair("status"
                    , new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox(
                    "approved")))));
            var approveRequest = new HandleRequestRequest(token, form);
            var approveResponse = new HandleRequestResponse(requestHandler.apply(approveRequest));
            if (approveResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully approved request",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AdminRequestsView(onChangeView, onExit, requestHandler, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to approve the request",
                        MessageDialogButton.Close);
            }
        } else if (res == MessageDialogButton.No) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("id",
                    new TerminalInputPair(new TerminalText("Request id"),
                            new TerminalImmutableTextBox(row.get(0)))), new TerminalFormKeyValuePair("status"
                    , new TerminalInputPair(new TerminalText("Status"),
                    new TerminalImmutableTextBox("denied")))));
            var denyRequest = new HandleRequestRequest(token, form);
            var denyResponse = new HandleRequestResponse(requestHandler.apply(denyRequest));
            if (denyResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully denied request",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AdminRequestsView(onChangeView, onExit, requestHandler, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to deny the request",
                        MessageDialogButton.Close);
            }
        }
    }
}
