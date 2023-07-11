package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class AdminActionsView implements IView{

    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;

    public AdminActionsView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
    }

    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        TerminalWindow window = new TerminalWindow("Account selection");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalButton("Manage offers", () -> {
            window.close();
            onChangeView.accept(new OfferManagementView(onChangeView, requestHandler, token));
        }).attachTo(contentPanel);

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
