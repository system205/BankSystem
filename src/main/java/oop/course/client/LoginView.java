package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class LoginView implements IView {
    private final Screen screen;
    private Consumer<Type> onSceneChange;

    public LoginView(Screen screen) throws IOException {
        this.screen = screen;
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show() {
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("BankSystem authentication");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        Label welcomeText = new Label("Welcome to the BankSystem client application!\nPlease, register or login into your existing account.");
        contentPanel.addComponent(welcomeText);
        contentPanel.addComponent(new Label("Username"));
        contentPanel.addComponent(new TextBox());
        contentPanel.addComponent(new Label("Password"));
        contentPanel.addComponent(new TextBox().setMask('*'));

        contentPanel.addComponent(new Button("Login", () -> {
            window.close();
            onSceneChange.accept(Type.Account);
        }));
        contentPanel.addComponent(new Button("Register page", () -> {
            window.close();
            onSceneChange.accept(Type.Register);
        }));
        contentPanel.addComponent(new Button("Exit", () -> {
            window.close();
            onSceneChange.accept(Type.None);
        }));
        window.setComponent(contentPanel);
        textGUI.addWindow(window);
    }

    @Override
    public void registerChangeViewHandler(Consumer<Type> consumer) {
        onSceneChange = consumer;
    }
}
