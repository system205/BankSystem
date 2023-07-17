package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.Transaction;

import java.util.List;

public class TerminalTransactionTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalTransactionTable(List<Transaction> transactions) {
        var columns = new String[]{"Type", "From", "Amount", "Date"};
        var rows =
                transactions.stream().map(x -> new String[]{x.type(), x.from(), x.amount(), x.date()}).toArray(x -> new String[x][1]);
        table = new TerminalTable(columns, rows, (List<String> row) -> {});
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
