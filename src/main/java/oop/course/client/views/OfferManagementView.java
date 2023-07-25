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
        var window = new TerminalWindow("Account selection", new Panel(new LinearLayout(Direction.VERTICAL)));
        var response = serverBridge.execute(new GetOffersRequest(token));
        if (response.isSuccess()) {
            new TerminalOffersTable(response.offers(), row -> onRowSelected(row, gui)).attachTo(window.panel());
        } else {
            new TerminalText(response.message()).attachTo(window.panel());
        }
        new TerminalButton("Return", this::onReturn).attachTo(window.panel());
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
