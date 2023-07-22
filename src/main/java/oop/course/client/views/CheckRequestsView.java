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

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class CheckRequestsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;

    public CheckRequestsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                             String token) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.onExit = onExit;
        this.token = token;
    }

    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        TerminalWindow window = new TerminalWindow("Requests list", contentPanel);

        var response = serverBridge.execute(new GetRequestsRequest(token));

        if (response.isSuccess()) {
            new TerminalBankRequestTable(response.requests(), (List<String> row) -> {
            }).attachTo(contentPanel);
        } else {
            new TerminalText("Could not fetch data from the server").attachTo(contentPanel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        window.addToGui(gui);
        window.open();
    }
}
