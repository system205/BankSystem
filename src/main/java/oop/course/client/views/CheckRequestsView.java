package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableModel;
import oop.course.client.BankRequest;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.GetRequestsRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.GetRequestsResponse;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CheckRequestsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;

    public CheckRequestsView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
    }
    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        TerminalWindow window = new TerminalWindow("Action selector");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        var request = new GetRequestsRequest(token);
        var response = new GetRequestsResponse(requestHandler.apply(request));

        if (response.isSuccess()) {
            List<BankRequest> requests = response.requests();
            var table = new Table<String>("Id", "Account Number", "Amount", "Type", "Status");
            var tableModel = new TableModel<String>("Id", "Account Number", "Amount", "Type", "Status");
            for (var row : requests) {
                tableModel.addRow(row.id(), row.accountNumber(), row.amount(), row.type(), row.status());
            }
            table.setTableModel(tableModel);
            table.addTo(contentPanel);
        }
        else {
            new TerminalText("Could not fetch data from the server").attachTo(contentPanel);
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