package oop.course.client.gui;

import com.googlecode.lanterna.gui2.*;

import java.util.*;
import java.util.function.*;

public final class TerminalOffersTable implements TerminalGUIElement {
    private final TerminalTable table;

    public TerminalOffersTable(List<List<String>> offers, Consumer<List<String>> onRowSelected) {
        var columns = new String[]{"Id", "Email", "Status", "Date"};
        String[][] rows = offers.stream().map(l -> l.toArray(String[]::new)).toArray(String[][]::new);
        table = new TerminalTable(columns, rows, onRowSelected);
    }

    @Override
    public void attachTo(Panel panel) {
        table.attachTo(panel);
    }
}
