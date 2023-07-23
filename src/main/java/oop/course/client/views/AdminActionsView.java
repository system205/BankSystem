package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.ServerBridge;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;

import java.util.function.Consumer;

public class AdminActionsView implements IView {

    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final TerminalWindow window;
    private final Panel contentPanel;
    private final String token;

    public AdminActionsView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                            String token) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Admin panel", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        new TerminalButton("Manage offers", this::onManageOffers).attachTo(contentPanel);
        new TerminalButton("View requests", this::onViewRequests).attachTo(contentPanel);
        new TerminalButton("Return", this::onReturn).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onManageOffers() {
        onChangeView.accept(new OfferManagementView(onChangeView, onExit, serverBridge, token));
    }

    private void onViewRequests() {
        onChangeView.accept(new AdminRequestsView(onChangeView, onExit, serverBridge, token));
    }

    private void onReturn() {
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }
}
