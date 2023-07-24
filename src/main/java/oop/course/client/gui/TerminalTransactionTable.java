package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;

import java.util.List;

public final class TerminalTransactionTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalTransactionTable(List<List<String>> transactions) {
        var columns = new String[]{"Type", "From", "Amount", "Date"};
        String[][] rows = transactions.stream().map(l -> l.toArray(String[]::new)).toArray(String[][]::new);
        table = new TerminalTable(columns, rows, (List<String> row) -> {});
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
