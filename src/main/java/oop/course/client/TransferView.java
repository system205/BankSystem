package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;

public class TransferView implements View {
    private final Screen screen;
    private ViewType nextView;

    public TransferView(Terminal terminal) throws IOException {
        screen = new TerminalScreen(terminal);
        nextView = ViewType.NoView;
    }

    @Override
    public ViewType show() throws IOException {
        screen.startScreen();
        WindowBasedTextGUI textGUI = new MultiWindowTextGUI(screen);
        Window window = new BasicWindow("Money transfer");
        window.setHints(List.of(Window.Hint.CENTERED));
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        contentPanel.addComponent(new Label("Please enter the requisites of the recipient."));
        contentPanel.addComponent(new Label("Account number:"));
        contentPanel.addComponent(new TextBox());
        contentPanel.addComponent(new Label("Transfer amount:"));
        contentPanel.addComponent(new TextBox());

        contentPanel.addComponent(new Button("Transfer money", () -> {
            nextView = ViewType.AccountView;
            MessageDialog.showMessageDialog(textGUI, "Success", "Successfully transferred money.", MessageDialogButton.OK);
            window.close();
        }));

        contentPanel.addComponent(new Button("Cancel", () -> {
            nextView = ViewType.AccountView;
            window.close();
        }));

        window.setComponent(contentPanel);
        textGUI.addWindowAndWait(window);
        return nextView;
    }
}
