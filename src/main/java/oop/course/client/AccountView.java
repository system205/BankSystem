package oop.course.client;

import com.googlecode.lanterna.gui2.*;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class AccountView implements IView {
    private Consumer<Type> onSceneChange;

    public AccountView() throws IOException {
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Account");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalText("Welcome to your account.").attachTo(contentPanel);
        new TerminalText("Name:").attachTo(contentPanel);
        new TerminalText("Ivan Ivanov").attachTo(contentPanel);
        new TerminalText("Account number:").attachTo(contentPanel);
        new TerminalText("123456").attachTo(contentPanel);
        new TerminalText("Balance").attachTo(contentPanel);
        new TerminalText("120.0$").attachTo(contentPanel);

        new TerminalButton("Transfer money", () -> {
            window.close();
            onSceneChange.accept(Type.Transfer);
        }).attachTo(contentPanel);

        new TerminalButton("Logout", () -> {
            window.close();
            onSceneChange.accept(Type.Login);
        }).attachTo(contentPanel);

        new TerminalButton("Logout & exit", () -> {
            window.close();
            onSceneChange.accept(Type.None);
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    @Override
    public void registerChangeViewHandler(Consumer<Type> consumer) {
        onSceneChange = consumer;
    }
}
