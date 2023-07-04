package oop.course.client;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.io.IOException;
import java.util.function.BiConsumer;

public class ActionSelectView implements IView{
    private BiConsumer<Type, String> onSceneChange;


    public ActionSelectView() throws IOException {
        onSceneChange = (Type type, String string) -> {};
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Money transfer");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please select an action.").attachTo(contentPanel);
        new TerminalButton("View accounts", () -> {}).attachTo(contentPanel);
        new TerminalButton("Transfer", () -> {}).attachTo(contentPanel);
        new TerminalButton("Create a request", () -> {}).attachTo(contentPanel);
        new TerminalButton("Logout", () -> onSceneChange.accept(Type.Login, "")).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    @Override
    public void registerChangeViewHandler(BiConsumer<Type, String> consumer) {
        onSceneChange = consumer;
    }
}
