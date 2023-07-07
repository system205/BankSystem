package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TransferView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String email;

    public TransferView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token, String email) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.email = email;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Money transfer");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        var sender = new TerminalFormKeyValuePair("sender", new TerminalInputPair(new TerminalText("Sender"), new TerminalFixedTextBox(email)));
        var receiver = new TerminalFormKeyValuePair("receiver", new TerminalInputPair(new TerminalText("Receiver"), new TerminalTextBox()));
        var sum = new TerminalFormKeyValuePair("sum", new TerminalInputPair(new TerminalText("Sum"), new TerminalTextBox()));

        sender.attachTo(contentPanel);
        receiver.attachTo(contentPanel);
        sum.attachTo(contentPanel);

        var form = new TerminalForm(List.of(sender, receiver, sum));

        new TerminalButton("Transfer money", () -> {
            System.out.println(form.json());
            MessageDialog.showMessageDialog(gui, "Success", "Successfully transferred money.", MessageDialogButton.OK);
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token, email));
        }).attachTo(contentPanel);

        new TerminalButton("Cancel", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token, email));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
