package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.GetRequestsRequest;

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
        var response = serverBridge.execute(new GetRequestsRequest(token));
        TerminalGUIElement requests;
        if (response.isSuccess()) {
            requests = new TerminalBankRequestTable(response.requests(), row -> {});
        } else {
            requests = new TerminalText(response.message());
        }

        var window = new TerminalWindow(
            "Requests list",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            requests,
            new TerminalButton("Return", this::onReturn)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
    }
}
