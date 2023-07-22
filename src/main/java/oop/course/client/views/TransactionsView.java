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

    public TransactionsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                            String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.accountNumber = accountNumber;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Account Statement request");
        var panel = new Panel(new LinearLayout(Direction.VERTICAL));

        var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("accountNumber",
                new TerminalInputPair(new TerminalText("Account Number"),
                        new TerminalImmutableTextBox(accountNumber)))));

        var response = serverBridge.execute(new TransactionsRequest(token, form));

        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(panel);
        } else {
            new TerminalTransactionTable(response.transactions()).attachTo(panel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(panel);

        window.setContent(panel);
        window.addToGui(gui);
        window.open();
    }
}
