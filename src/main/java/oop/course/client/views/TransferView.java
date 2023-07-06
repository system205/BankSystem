package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalTextBox;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public class TransferView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;

    public TransferView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
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
            //onChangeView.accept(new AccountsView(onChangeView, requestHandler));
        }).attachTo(contentPanel);

        new TerminalButton("Cancel", () -> {
            window.close();
            //onChangeView.accept(Type.Account);
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
