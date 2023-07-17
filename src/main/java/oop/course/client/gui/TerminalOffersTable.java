package oop.course.client.gui;

import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.Offer;

import java.util.List;
import java.util.function.Consumer;

public class TerminalOffersTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalOffersTable(List<Offer> offers, Consumer<List<String>> onRowSelected) {
        var columns = new String[]{"Id", "Email", "Status", "Date"};
        var rows =
                offers.stream().map(x -> new String[]{x.id(), x.email(), x.status(), x.date()}).toArray(x -> new String[x][1]);
        table = new TerminalTable(columns, rows, onRowSelected);
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
