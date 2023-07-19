package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.GetOffersRequest;
import oop.course.client.requests.HandleOfferRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.GetOffersResponse;
import oop.course.client.responses.HandleOfferResponse;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class OfferManagementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public OfferManagementView(Consumer<IView> changeViewHandler, Runnable onExit, Function<Request, BasicResponse> requestHandler,
                               String token) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.onExit = onExit;
        this.token = token;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        this.window = new TerminalWindow("Account selection", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        Request req = new GetOffersRequest(token);
        var response = new GetOffersResponse(requestHandler.apply(req));

        if (response.isSuccess()) {
            new TerminalOffersTable(response.offers(), (List<String> row) -> onRowSelected(row, gui)).attachTo(contentPanel);
        }
        else {
            new TerminalText("Could not fetch data from the server").attachTo(contentPanel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, requestHandler, token));
        }).attachTo(contentPanel);

        window.addToGui(gui);
        window.open();
    }

    private void onRowSelected(List<String> offer, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui, "Select an action", "Do you want to approve an " +
                "offer?", MessageDialogButton.Yes, MessageDialogButton.No, MessageDialogButton.Cancel);
        if (res == MessageDialogButton.Yes) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("customerEmail",
                    new TerminalInputPair(new TerminalText("Customer email"),
                            new TerminalImmutableTextBox(offer.get(1)))), new TerminalFormKeyValuePair("status"
                    , new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox(
                    "accepted")))));
            var acceptRequest = new HandleOfferRequest(token, form);
            var acceptResponse = new HandleOfferResponse(requestHandler.apply(acceptRequest));
            if (acceptResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully approved an offer",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AdminRequestsView(onChangeView, onExit, requestHandler, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to approve the request",
                        MessageDialogButton.Close);
            }
        } else if (res == MessageDialogButton.No) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("customerEmail",
                    new TerminalInputPair(new TerminalText("Customer email"),
                            new TerminalImmutableTextBox(offer.get(1)))), new TerminalFormKeyValuePair("status"
                    , new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox(
                    "rejected")))));
            var rejectRequest = new HandleOfferRequest(token, form);
            var rejectResponse = new HandleOfferResponse(requestHandler.apply(rejectRequest));
            if (rejectResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully denied an offer",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AdminRequestsView(onChangeView, onExit, requestHandler, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to deny the offer",
                        MessageDialogButton.Close);
            }
        }
    }
}
