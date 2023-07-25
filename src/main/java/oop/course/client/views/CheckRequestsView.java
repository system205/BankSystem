package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.TerminalBankRequestTable;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.GetRequestsRequest;

import java.util.List;
import java.util.function.Consumer;

public final class CheckRequestsView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;

    public CheckRequestsView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
                             String token) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.exitAction = exitAction;
        this.token = token;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Requests list", new Panel(new LinearLayout(Direction.VERTICAL)));
        var response = serverBridge.execute(new GetRequestsRequest(token));
        if (response.isSuccess()) {
            response.fillRequestsTable(
                    (List<List<String>> rows) -> new TerminalBankRequestTable(rows, (List<String> row) -> {})
            ).attachTo(window.panel());
        } else {
            new TerminalText(response.message()).attachTo(window.panel());
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
