package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;

import java.util.List;
import java.util.function.Consumer;

public class StatementInputView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;

    public StatementInputView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                              String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.accountNumber = accountNumber;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var panel = new Panel(new LinearLayout(Direction.VERTICAL));
        var window = new TerminalWindow("Account Statement request", panel);

        var accKV = new TerminalFormKeyValuePair("accountNumber",
                new TerminalInputPair(new TerminalText("Account " + "Number"),
                        new TerminalImmutableTextBox(accountNumber)));
        var startDate = new TerminalFormKeyValuePair("startDate", new TerminalInputPair(new TerminalText("Start date "
                + "(YYYY-MM-DD format)"), new TerminalTextBox()));
        var endDate = new TerminalFormKeyValuePair("endDate", new TerminalInputPair(new TerminalText("End date " +
                "(YYYY-MM-DD format)"), new TerminalTextBox()));

        accKV.attachTo(panel);
        startDate.attachTo(panel);
        endDate.attachTo(panel);

        var form = new TerminalForm(List.of(accKV, startDate, endDate));

        new TerminalButton("Request", () -> {
            window.close();
            onChangeView.accept(new StatementView(onChangeView, onExit, serverBridge, token, form));
        }).attachTo(panel);

        new TerminalButton("Cancel", () -> {
            window.close();
            onChangeView.accept(new AccountActionsView(onChangeView, onExit, serverBridge, token, accountNumber));
        }).attachTo(panel);

        window.addToGui(gui);
        window.open();
    }
}
