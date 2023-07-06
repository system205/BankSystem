package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.actions.Action;
import oop.course.client.actions.ChangeSceneAction;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalWindow;

import java.util.function.Consumer;

public class ActionSelectView implements IView {
    private final Consumer<Action> actionConsumer;


    public ActionSelectView(Consumer<Action> actionHandler) {
        actionConsumer = actionHandler;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Action selector");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please select an action.").attachTo(contentPanel);
        new TerminalButton("View accounts", () -> {}).attachTo(contentPanel);
        new TerminalButton("Transfer", () -> {}).attachTo(contentPanel);
        new TerminalButton("Create a request", () -> {}).attachTo(contentPanel);
        new TerminalButton("Logout", () -> actionConsumer.accept(new ChangeSceneAction(Type.Login))).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
