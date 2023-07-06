package oop.course.client;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.util.function.Consumer;

public class TransferView implements IView {
    private final Consumer<Action> actionConsumer;

    public TransferView(Consumer<Action> actionHandler) {
        actionConsumer = actionHandler;
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
            actionConsumer.accept(new ChangeSceneAction(Type.Account));
        }).attachTo(contentPanel);

        new TerminalButton("Cancel", () -> {
            window.close();
            actionConsumer.accept(new ChangeSceneAction(Type.Account));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
