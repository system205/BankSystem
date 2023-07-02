package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.io.IOException;
import java.util.function.Consumer;

public class TransferView implements IView {
    private Consumer<Type> onSceneChange;

    public TransferView() throws IOException {
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Money transfer");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalText("Please enter the requisites of the recipient.").attachTo(contentPanel);
        new TerminalText("Account number:").attachTo(contentPanel);
        new TerminalTextBox().attachTo(contentPanel);
        new TerminalText("Transfer amount:").attachTo(contentPanel);
        new TerminalTextBox().attachTo(contentPanel);

        new TerminalButton("Transfer money", () -> {
            MessageDialog.showMessageDialog(gui, "Success", "Successfully transferred money.", MessageDialogButton.OK);
            window.close();
            onSceneChange.accept(Type.Login);
        }).attachTo(contentPanel);

        new TerminalButton("Cancel", () -> {
            window.close();
            onSceneChange.accept(Type.Account);
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
