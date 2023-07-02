package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class RegisterView implements IView {
    private Consumer<Type> onSceneChange;


    public RegisterView() throws IOException {
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem registration");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);
        new TerminalText("Username").attachTo(contentPanel);
        new TerminalTextBox().attachTo(contentPanel);
        new TerminalText("Password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);
        new TerminalText("Repeat password").attachTo(contentPanel);
        new TerminalPasswordBox().attachTo(contentPanel);
        new TerminalButton("Register", () ->
                MessageDialog.showMessageDialog(gui, "Success", "You may now login into your account.", MessageDialogButton.OK)
        ).attachTo(contentPanel);
        new TerminalButton("Back to login page", () -> {
            window.close();
            onSceneChange.accept(Type.Login);
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onSceneChange.accept(Type.None);
        });

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    @Override
    public void registerChangeViewHandler(Consumer<Type> consumer) {
        onSceneChange = consumer;
    }
}
