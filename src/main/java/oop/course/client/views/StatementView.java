package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.StatementRequest;

import java.util.function.Consumer;

public final class StatementView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalForm form;

    public StatementView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge, String token,
                         TerminalForm form) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
        this.form = form;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Account Statement",  new Panel(new LinearLayout(Direction.VERTICAL)));
        var response = serverBridge.execute(new StatementRequest(token, form.json()));
        if (!response.isSuccess()) {
            new TerminalText(response.message()).attachTo(window.panel());
        } else {
            new TerminalText("Starting balance for the period: " + response.startingBalance()).attachTo(window.panel());
            new TerminalText("Ending balance for the period: " + response.endingBalance()).attachTo(window.panel());
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
