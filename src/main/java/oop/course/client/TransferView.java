package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class TransferView implements IView {
    private final Screen screen;
    private Consumer<Type> onSceneChange;

    public TransferView(Screen screen) throws IOException {
        this.screen = screen;
        onSceneChange = (Type type) -> {};
    }

    @Override
    public void show() {
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
            MessageDialog.showMessageDialog(textGUI, "Success", "Successfully transferred money.", MessageDialogButton.OK);
            window.close();
            onSceneChange.accept(Type.Login);
        }));

        contentPanel.addComponent(new Button("Cancel", () -> {
            window.close();
            onSceneChange.accept(Type.Account);
        }));

        window.setComponent(contentPanel);
        textGUI.addWindow(window);
    }

    @Override
    public void registerChangeViewHandler(Consumer<Type> consumer) {
        onSceneChange = consumer;
    }
}
