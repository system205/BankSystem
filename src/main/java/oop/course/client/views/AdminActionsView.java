package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;

import java.util.function.Consumer;

public final class AdminActionsView implements IView {

    private final Consumer<IView> changeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;

    public AdminActionsView(Consumer<IView> changeView, Runnable onExit, ServerBridge serverBridge,
                            String token) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow(
            "Admin panel",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            new TerminalButton("Manage offers", this::onManageOffers),
            new TerminalButton("View requests", this::onViewRequests),
            new TerminalButton("Return", this::onReturn)
        );
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onManageOffers() {
        changeView.accept(new OfferManagementView(changeView, onExit, serverBridge, token));
    }

    private void onViewRequests() {
        changeView.accept(new AdminRequestsView(changeView, onExit, serverBridge, token));
    }

    private void onReturn() {
        changeView.accept(new AccountsView(changeView, onExit, serverBridge, token));
    }
}
