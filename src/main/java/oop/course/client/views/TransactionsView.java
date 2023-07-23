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

public class TransactionsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public TransactionsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                            String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.accountNumber = accountNumber;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Account transactions", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("accountNumber",
                new TerminalInputPair(new TerminalText("Account Number"),
                        new TerminalImmutableTextBox(accountNumber)))));
        var response = serverBridge.execute(new TransactionsRequest(token, form.json()));
        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(contentPanel);
        } else {
            response.fillTransactionsTable(TerminalTransactionTable::new).attachTo(contentPanel);
        }
        new TerminalButton("Return", this::onReturn).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    private void onReturn() {
        window.close();
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }
}
