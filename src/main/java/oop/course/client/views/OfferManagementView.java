package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.GetOffersRequest;
import oop.course.client.requests.HandleOfferRequest;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class OfferManagementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalWindow window;

    public OfferManagementView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                               String token) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.onExit = onExit;
        this.token = token;
        this.window = new TerminalWindow("Account selection");
    }

    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        var response = serverBridge.execute(new GetOffersRequest(token));

        if (response.isSuccess()) {
            new TerminalOffersTable(response.offers(), (List<String> row) -> onRowSelected(row, gui)).attachTo(contentPanel);
        } else {
            new TerminalText("Could not fetch data from the server").attachTo(contentPanel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    private void onRowSelected(List<String> offer, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui, "Select an action", "Do you want to approve an " + "offer?",
                MessageDialogButton.Yes, MessageDialogButton.No, MessageDialogButton.Cancel);
        if (res == MessageDialogButton.Yes) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("customerEmail",
                    new TerminalInputPair(new TerminalText("Customer email"),
                            new TerminalImmutableTextBox(offer.get(1)))), new TerminalFormKeyValuePair("status",
                    new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox("accepted")))));
            var acceptResponse = serverBridge.execute(new HandleOfferRequest(token, form));
            if (acceptResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully approved an offer",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AdminRequestsView(onChangeView, onExit, serverBridge, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to approve the request",
                        MessageDialogButton.Close);
            }
        } else if (res == MessageDialogButton.No) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("customerEmail",
                    new TerminalInputPair(new TerminalText("Customer email"),
                            new TerminalImmutableTextBox(offer.get(1)))), new TerminalFormKeyValuePair("status",
                    new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox("rejected")))));
            var rejectResponse = serverBridge.execute(new HandleOfferRequest(token, form));
            if (rejectResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully denied an offer", MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AdminRequestsView(onChangeView, onExit, serverBridge, token));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to deny the offer", MessageDialogButton.Close);
            }
        }
    }
}
