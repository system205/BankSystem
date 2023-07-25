package oop.course.client.gui;

import com.googlecode.lanterna.gui2.ComboBox;
import com.googlecode.lanterna.gui2.Panel;

import java.util.List;

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
