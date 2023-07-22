package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;

import java.util.List;
import java.util.function.Consumer;

public class TerminalAccountsTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalAccountsTable(List<List<String>> accounts, Consumer<List<String>> onRowSelected) {
        var columns = new String[]{"Id", "Balance"};
        String[][] rows = accounts.stream().map(l -> l.toArray(String[]::new)).toArray(String[][]::new);
        table = new TerminalTable(columns, rows, onRowSelected);
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
