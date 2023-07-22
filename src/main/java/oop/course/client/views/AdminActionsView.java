package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;

import java.io.IOException;
import java.util.function.Consumer;

public class AdminActionsView implements IView {

    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;

    public AdminActionsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                            String token) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
    }

    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        TerminalWindow window = new TerminalWindow("Account selection");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalButton("Manage offers", () -> {
            window.close();
            onChangeView.accept(new OfferManagementView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        new TerminalButton("View requests", () -> {
            window.close();
            onChangeView.accept(new AdminRequestsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
