package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.*;
import oop.course.client.requests.Request;
import oop.course.client.requests.StatementRequest;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.StatementResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public class StatementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final TerminalForm form;

    public StatementView(Consumer<IView> changeViewHandler, Runnable onExit, Function<Request, BasicResponse> requestHandler,
                         String token, TerminalForm form) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.onExit = onExit;
        this.form = form;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        TerminalWindow window = new TerminalWindow("Account Statement", contentPanel);

        var request = new StatementRequest(token, form);
        var response = new StatementResponse(requestHandler.apply(request));

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
            onChangeView.accept(new AccountsView(onChangeView, onExit, requestHandler, token));
        }).attachTo(contentPanel);

        window.addToGui(gui);
        window.open();
    }
}
