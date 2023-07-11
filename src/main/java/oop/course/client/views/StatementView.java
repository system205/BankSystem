package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import oop.course.client.Transaction;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalForm;
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.Request;
import oop.course.client.requests.StatementRequest;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.StatementResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class StatementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final TerminalForm form;

    public StatementView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token, TerminalForm form) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.form = form;
    }


    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Account Statement");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        var request = new StatementRequest(token, form);
        var response = new StatementResponse(requestHandler.apply(request));

        if (!response.isSuccess()) {
            new TerminalText("Could not fetch data").attachTo(contentPanel);
        }
        else {
            //experimental raw lanterna table
            new TerminalText("Starting balance for the period: " + response.startingBalance()).attachTo(contentPanel);
            new TerminalText("Ending balance for the period: " + response.endingBalance()).attachTo(contentPanel);
            List<Transaction> transactions = response.transactions();
            var table = new Table<String>("Type", "From", "Amount", "Date");
            var tableModel = new TableModel<String>("Type", "From", "Amount", "Date");
            for (var row : transactions) {
                tableModel.addRow(row.type(), row.from(), row.amount(), row.date());
            }
            table.setTableModel(tableModel);
            table.addTo(contentPanel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
