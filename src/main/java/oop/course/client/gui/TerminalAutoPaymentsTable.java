package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;

import java.util.List;
import java.util.function.Consumer;

public final class TerminalAutoPaymentsTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalAutoPaymentsTable(List<List<String>> autopayments, Consumer<List<String>> onRowSelected) {
        var columns = new String[]{"Id", "Sender", "Receiver", "Amount", "Start Date", "Period"};
        var rows = autopayments.stream().map(x -> new String[]{
            x.get(0),
            x.get(1),
            x.get(2),
            x.get(3),
            x.get(4),
            x.get(5)}
        ).toArray(x -> new String[x][1]);
        table = new TerminalTable(columns, rows, onRowSelected);
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
