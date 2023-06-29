package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class RegisterView implements IView {
    private final Screen screen;
    private Consumer<Type> onSceneChange;


    public RegisterView(Screen screen) throws IOException {
        this.screen = screen;
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show() {
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("BankSystem registration");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);
        new TerminalText("Username").attachTo(contentPanel);
        new TerminalTextBox().attachTo(contentPanel);
        new TerminalText("Password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);
        new TerminalText("Repeat password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);
        new TerminalButton("Register", () ->
                MessageDialog.showMessageDialog(textGUI, "Success", "You may now login into your account.", MessageDialogButton.OK)
        ).attachTo(contentPanel);
        new TerminalButton("Back to login page", () -> {
            window.close();
            onSceneChange.accept(Type.Login);
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onSceneChange.accept(Type.None);
        });

        window.setComponent(contentPanel);
        textGUI.addWindow(window);
    }

    @Override
    public void registerChangeViewHandler(Consumer<Type> consumer) {
        onSceneChange = consumer;
    }
}
