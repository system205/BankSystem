package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.*;
import oop.course.client.requests.Request;
import oop.course.client.requests.TransactionsRequest;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.TransactionsResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String accountNumber;

    public TransactionsView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler,
                            String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.accountNumber = accountNumber;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Account Statement request");
        var panel = new Panel(new LinearLayout(Direction.VERTICAL));

        var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("accountNumber",
                new TerminalInputPair(new TerminalText("Account Number"),
                        new TerminalImmutableTextBox(accountNumber)))));

        var request = new TransactionsRequest(token, form);
        var response = new TransactionsResponse(requestHandler.apply(request));

        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(panel);
        } else {
            //experimental raw lanterna table
            new TerminalTransactionTable(response.transactions()).attachTo(panel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token));
        }).attachTo(panel);

        window.setContent(panel);
        window.addToGui(gui);
        window.open();
    }
}
