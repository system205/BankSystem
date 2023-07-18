package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.BankRequest;

import java.util.List;
import java.util.function.Consumer;

public class TerminalBankRequestTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalBankRequestTable(List<BankRequest> transactions, Consumer<List<String>> onRowSelected) {
        var columns = new String[]{"Id", "Account Number", "Amount", "Type", "Status"};
        var rows = transactions.stream().map(x -> new String[]{x.id(), x.accountNumber(), x.amount(), x.type(),
                x.status()}).toArray(x -> new String[x][1]);
        table = new TerminalTable(columns, rows, onRowSelected);
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
