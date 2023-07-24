package oop.course.client;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.views.IView;

import java.util.List;

public class TerminalGUI implements GUI {
    private final List<IView> views;
    private final WindowBasedTextGUI gui;

    public TerminalGUI(MultiWindowTextGUI gui, List<IView> views) {
        this.views = views;
        this.gui = gui;
    }

    @Override
    public void startLooping(ServerBridge serverBridge) {
        views.get(0).show(gui);
    }
}
