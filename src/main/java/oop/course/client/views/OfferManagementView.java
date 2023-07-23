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

import java.util.List;
import java.util.function.Consumer;

public class OfferManagementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public OfferManagementView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                               String token) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.onExit = onExit;
        this.token = token;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Account selection", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new GetOffersRequest(token));
        if (response.isSuccess()) {
            response.fillOffersTable((List<List<String>> rows) -> new TerminalOffersTable(rows,
                    (List<String> row) -> onRowSelected(row, gui))).attachTo(contentPanel);
        } else {
            new TerminalText(response.message()).attachTo(contentPanel);
        }
        new TerminalButton("Return", this::onReturn).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }

    private void onRowSelected(List<String> offer, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui, "Select an action", "Do you want to approve an offer?",
                MessageDialogButton.Yes, MessageDialogButton.No, MessageDialogButton.Cancel);
        TerminalForm form;
        if (res == MessageDialogButton.Yes) {
            form = new TerminalForm(List.of(new TerminalFormKeyValuePair("customerEmail",
                    new TerminalInputPair(new TerminalText("Customer email"),
                            new TerminalImmutableTextBox(offer.get(1)))), new TerminalFormKeyValuePair("status",
                    new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox("accepted")))));
        } else if (res == MessageDialogButton.No) {
            form = new TerminalForm(List.of(new TerminalFormKeyValuePair("customerEmail",
                    new TerminalInputPair(new TerminalText("Customer email"),
                            new TerminalImmutableTextBox(offer.get(1)))), new TerminalFormKeyValuePair("status",
                    new TerminalInputPair(new TerminalText("Status"), new TerminalImmutableTextBox("rejected")))));
        } else {
            return;
        }
        var response = serverBridge.execute(new HandleOfferRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            onChangeView.accept(new AdminRequestsView(onChangeView, onExit, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }
}
