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
        Window window = new BasicWindow("Account");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        contentPanel.addComponent(new Label("Welcome to your account."));
        contentPanel.addComponent(new Label("Name:"));
        contentPanel.addComponent(new Label("Ivan Ivanov"));
        contentPanel.addComponent(new Label("Account number:"));
        contentPanel.addComponent(new Label("123456"));
        contentPanel.addComponent(new Label("Balance"));
        contentPanel.addComponent(new Label("120.0$"));

        contentPanel.addComponent(new Button("Transfer money", () -> {
            window.close();
            onSceneChange.accept(Type.Transfer);
        }));

        contentPanel.addComponent(new Button("Logout", () -> {
            window.close();
            onSceneChange.accept(Type.Login);
        }));

        contentPanel.addComponent(new Button("Logout & exit", () -> {
            window.close();
            onSceneChange.accept(Type.None);
        }));

        window.setComponent(contentPanel);
        gui.addWindow(window);
    }

    @Override
    public void registerChangeViewHandler(Consumer<Type> consumer) {
        onSceneChange = consumer;
    }
}
