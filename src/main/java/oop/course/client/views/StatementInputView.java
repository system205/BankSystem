package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;

import java.util.List;
import java.util.function.Consumer;

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
        var window = new TerminalWindow("Account Statement request", new Panel(new LinearLayout(Direction.VERTICAL)));
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
        form.attachTo(window.panel());

        new TerminalButton("Request", () -> onRequest(form)).attachTo(window.panel());
        new TerminalButton("Cancel", this::onCancel).attachTo(window.panel());
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
