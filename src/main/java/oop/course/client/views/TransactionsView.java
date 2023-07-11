package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import oop.course.client.Transaction;
import oop.course.client.gui.*;
import oop.course.client.requests.Request;
import oop.course.client.requests.TransactionsRequest;
import oop.course.client.responses.BasicResponse;
import oop.course.responses.TransactionsResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransactionsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String accountNumber;

    public TransactionsView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.accountNumber = accountNumber;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Account Statement request");
        var panel = new Panel(new LinearLayout(Direction.VERTICAL));

        var form = new TerminalForm(
                List.of(new TerminalFormKeyValuePair("accountNumber",
                        new TerminalInputPair(new TerminalText("Account Number"),
                                new TerminalFixedTextBox(accountNumber)))));

        var request = new TransactionsRequest(token, form);
        var response = new TransactionsResponse(requestHandler.apply(request));

        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(panel);
        }
        else {
            //experimental raw lanterna table
            List<Transaction> transactions = response.transactions();
            var table = new Table<String>("Type", "From", "Amount", "Date");
            var tableModel = new TableModel<String>("Type", "From", "Amount", "Date");
            for (var row : transactions) {
                tableModel.addRow(row.type(), row.from(), row.amount(), row.date());
            }
            table.setTableModel(tableModel);
            table.addTo(panel);
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
