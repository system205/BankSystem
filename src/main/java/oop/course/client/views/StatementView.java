package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.StatementRequest;

import java.util.function.Consumer;

public class StatementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalForm form;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public StatementView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge, String token,
                         TerminalForm form) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.form = form;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Account Statement", contentPanel);
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        var response = serverBridge.execute(new StatementRequest(token, form.json()));
        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(contentPanel);
        } else {
            new TerminalText("Starting balance for the period: " + response.startingBalance()).attachTo(contentPanel);
            new TerminalText("Ending balance for the period: " + response.endingBalance()).attachTo(contentPanel);
            response.fillTransactionsTable(TerminalTransactionTable::new).attachTo(contentPanel);
        }
        new TerminalButton("Return", this::onReturn).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        window.close();
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }
}
