package oop.course.client.gui;

import com.googlecode.lanterna.gui2.*;

import java.util.*;

public final class DropDownTextBox implements TerminalInputBox {
    private final ComboBox<String> comboBox;

    public DropDownTextBox(List<String> options) {
        comboBox = new ComboBox<>(options);
    }

    @Override
    public void attachTo(Panel panel) {
        comboBox.addTo(panel);
    }

    @Override
    public String text() {
        return comboBox.getText();
    }
}
