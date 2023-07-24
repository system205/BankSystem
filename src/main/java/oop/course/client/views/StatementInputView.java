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
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public StatementInputView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                              String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.accountNumber = accountNumber;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Account Statement request", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "accountNumber",
                                new TerminalInputPair(
                                        new TerminalText("Account Number"),
                                        new TerminalImmutableTextBox(accountNumber)
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
        form.attachTo(contentPanel);

        new TerminalButton("Request", () -> onRequest(form)).attachTo(contentPanel);
        new TerminalButton("Cancel", this::onCancel).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onRequest(TerminalForm form) {
        onChangeView.accept(new StatementView(onChangeView, onExit, serverBridge, token, form));
    }

    private void onCancel() {
        onChangeView.accept(new AccountActionsView(onChangeView, onExit, serverBridge, token, accountNumber));
    }
}
