package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

public final class ListAutoPaymentsView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String account;

    public ListAutoPaymentsView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
                                String token, String account) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
        this.account = account;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        var form = new TerminalForm(
            List.of(
                new TerminalFormKeyValuePair(
                    "accountNumber",
                    new TerminalInputPair(
                        new TerminalText("Account number"),
                        new TerminalFixedTextBox(account)
                    )
                )
            )
        );
        var response = serverBridge.execute(new ListAutoPaymentsRequest(token, form.json()));
        TerminalGUIElement element;
        if (response.isSuccess()) {
            element = new TerminalAutoPaymentsTable(response.autopayments(), row -> onRowSelected(row, gui));
        } else {
            element = new TerminalText(response.message());
        }

        var window = new TerminalWindow(
            "Autopayments",
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

    private void onRowSelected(List<String> row, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui,
            "Select an action", "Do you want to cancel the auto-payment?",
            MessageDialogButton.Yes, MessageDialogButton.No);
        if (res == MessageDialogButton.Yes) {
            var form = new TerminalForm(
                List.of(
                    new TerminalFormKeyValuePair(
                        "paymentId",
                        new TerminalInputPair(
                            new TerminalText("Payment Id"),
                            new TerminalFixedTextBox(row.get(0))
                        )
                    )
                )
            );
            var deleteResponse = serverBridge.execute(new DeleteAutoPaymentRequest(token, form.json()));
            if (deleteResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", deleteResponse.message(), MessageDialogButton.OK);
                changeView.accept(new ListAutoPaymentsView(changeView, exitAction, serverBridge, token, account));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", deleteResponse.message(), MessageDialogButton.Close);
            }
        }
    }
}
