package oop.course.client;

import com.googlecode.lanterna.gui2.*;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class LoginView implements IView {
    private Consumer<Type> onSceneChange;

    public LoginView() throws IOException {
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem authentication");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText(
                "Welcome to the BankSystem client application!\nPlease, register or login into your existing account."
        ).attachTo(contentPanel);
        new TerminalText("Username").attachTo(contentPanel);
        new TerminalTextBox().attachTo(contentPanel);
        new TerminalText("Password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);

        new TerminalButton("Login", () -> {
            window.close();
            onSceneChange.accept(Type.Account);
        }).attachTo(contentPanel);

        new TerminalButton("Register page", () -> {
            window.close();
            onSceneChange.accept(Type.Register);
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
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
