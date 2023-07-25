package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.DeleteAutoPaymentRequest;
import oop.course.client.requests.ListAutoPaymentsRequest;

import java.util.List;
import java.util.function.Consumer;

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
        var window = new TerminalWindow("Autopayments", new Panel(new LinearLayout(Direction.VERTICAL)));
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
        if (response.isSuccess()) {
            response.fillAutopayments(
                    (List<List<String>> rows) -> new TerminalAutoPaymentsTable(rows, (List<String> row) -> onRowSelected(row, gui))
            ).attachTo(window.panel());
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
