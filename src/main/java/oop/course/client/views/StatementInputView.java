package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import oop.course.client.*;
import oop.course.client.gui.*;

import java.util.*;
import java.util.function.*;

public final class StatementInputView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;

    public StatementInputView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
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
                ),
                new TerminalFormKeyValuePair("startDate",
                    new TerminalInputPair(
                        new TerminalText("Start date (YYYY-MM-DD format)"),
                        new TerminalTextBox()
                    )
                ),
                new TerminalFormKeyValuePair(
                    "endDate",
                    new TerminalInputPair(
                        new TerminalText("End date (YYYY-MM-DD format)"),
                        new TerminalTextBox()
                    )
                )
            )
        );

        var window = new TerminalWindow(
            "Account Statement request",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            form,
            new TerminalButton("Request", () -> onRequest(form)),
            new TerminalButton("Cancel", this::onCancel)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onRequest(TerminalForm form) {
        changeView.accept(new StatementView(changeView, exitAction, serverBridge, token, form));
    }

    private void onCancel() {
        changeView.accept(new AccountActionsView(changeView, exitAction, serverBridge, token, accountNumber));
    }
}
