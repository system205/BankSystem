package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

public final class OfferManagementView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;

    public OfferManagementView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
                               String token) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.exitAction = exitAction;
        this.token = token;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new GetOffersRequest(token));
        TerminalGUIElement element;
        if (response.isSuccess()) {
            element = new TerminalOffersTable(response.offers(), row -> onRowSelected(row, gui));
        } else {
            element = new TerminalText(response.message());
        }

        var window = new TerminalWindow(
            "Account selection",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            element,
            new TerminalButton("Return", this::onReturn)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
    }

    private void onRowSelected(List<String> offer, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui,
            "Select an action", "Do you want to approve an offer?",
            MessageDialogButton.Yes, MessageDialogButton.No, MessageDialogButton.Cancel);
        TerminalForm form;
        if (res == MessageDialogButton.Yes) {
            form = new TerminalForm(
                List.of(
                    new TerminalFormKeyValuePair(
                        "customerEmail",
                        new TerminalInputPair(
                            new TerminalText("Customer email"),
                            new TerminalFixedTextBox(offer.get(1)
                            )
                        )
                    ),
                    new TerminalFormKeyValuePair(
                        "status",
                        new TerminalInputPair(
                            new TerminalText("Status"),
                            new TerminalFixedTextBox("accepted")
                        )
                    )
                )
            );
        } else if (res == MessageDialogButton.No) {
            form = new TerminalForm(
                List.of(
                    new TerminalFormKeyValuePair(
                        "customerEmail",
                        new TerminalInputPair(
                            new TerminalText("Customer email"),
                            new TerminalFixedTextBox(offer.get(1)
                            )
                        )
                    ),
                    new TerminalFormKeyValuePair(
                        "status",
                        new TerminalInputPair(
                            new TerminalText("Status"),
                            new TerminalFixedTextBox("rejected")
                        )
                    )
                )
            );
        } else {
            return;
        }
        var response = serverBridge.execute(new HandleOfferRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            changeView.accept(new AdminRequestsView(changeView, exitAction, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }
}
