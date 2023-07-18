package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.AutoPayment;

import java.util.List;
import java.util.function.Consumer;

public class TerminalAutoPaymentsTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalAutoPaymentsTable(List<AutoPayment> autopayments, Consumer<List<String>> onRowSelected) {
        var columns = new String[]{"Id", "Sender", "Receiver", "Amount", "Start Date", "Period"};
        var rows = autopayments.stream().map(x -> new String[]{x.id(), x.sender(), x.receiver(), x.amount(),
                x.startDate(), x.period()}).toArray(x -> new String[x][1]);
        table = new TerminalTable(columns, rows, onRowSelected);
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
