package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

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
        TerminalGUIElement element;
        if (!response.isSuccess()) {
            element = new TerminalText(response.message());
        } else {
            element = new TerminalTransactionTable(response.transactions());
        }
        var window = new TerminalWindow(
            "Account transactions",
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
}
