package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.table.Table;

import java.util.List;
import java.util.function.Consumer;

public class TerminalTable implements TerminalGUIElement {
    private final Table<String> table;

    public TerminalTable(String[] columns, String[][] rows, Consumer<List<String>> onRowSelected) {
        table = new Table<>(columns);
        for (var row : rows) {
            table.getTableModel().addRow(row);
        }
        table.setSelectAction(() -> onRowSelected.accept(table.getTableModel().getRow(table.getSelectedRow())));
    }

    @Override
    public void attachTo(Panel panel) {
        table.addTo(panel);
    }
}
