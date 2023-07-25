package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.TransactionsRequest;

import java.util.List;
import java.util.function.Consumer;

public final class TransactionsView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;

    public TransactionsView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
                            String token, String accountNumber) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
        this.accountNumber = accountNumber;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Account transactions", new Panel(new LinearLayout(Direction.VERTICAL)));
        var form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "accountNumber",
                                new TerminalInputPair(
                                        new TerminalText("Account Number"),
                                        new TerminalFixedTextBox(accountNumber)
                                )
                        )
                )
        );
        var response = serverBridge.execute(new TransactionsRequest(token, form.json()));
        if (!response.isSuccess()) {
            new TerminalText(response.message()).attachTo(window.panel());
        } else {
            response.fillTransactionsTable(TerminalTransactionTable::new).attachTo(window.panel());
        }
        new TerminalButton("Return", this::onReturn).attachTo(window.panel());
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
    }
}
