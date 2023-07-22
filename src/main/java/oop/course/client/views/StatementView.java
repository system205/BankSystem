package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.Transaction;
import oop.course.client.gui.*;
import oop.course.client.requests.StatementRequest;

import java.util.function.Consumer;

public class StatementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final TerminalForm form;

    public StatementView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge, String token,
                         TerminalForm form) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.form = form;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        TerminalWindow window = new TerminalWindow("Account Statement", contentPanel);

        var response = serverBridge.execute(new StatementRequest(token, form));

        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(contentPanel);
        } else {
            //experimental raw lanterna table
            new TerminalText("Starting balance for the period: " + response.startingBalance()).attachTo(contentPanel);
            new TerminalText("Ending balance for the period: " + response.endingBalance()).attachTo(contentPanel);
            response.transactionsTable().attachTo(contentPanel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        window.addToGui(gui);
        window.open();
    }
}
