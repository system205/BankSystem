package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.TransferRequest;

import java.util.List;
import java.util.function.Consumer;

public final class TransferView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;

    public TransferView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge, String token,
                        String accountNumber) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
        this.accountNumber = accountNumber;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var window = new TerminalWindow("Money transfer", new Panel(new LinearLayout(Direction.VERTICAL)));
        var form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "senderAccount",
                                new TerminalInputPair(
                                        new TerminalText("Sender"),
                                        new TerminalFixedTextBox(accountNumber)
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "receiverAccount",
                                new TerminalInputPair(
                                        new TerminalText("Receiver"),
                                        new TerminalTextBox()
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "amount",
                                new TerminalInputPair(
                                        new TerminalText("Sum"),
                                        new TerminalTextBox()
                                )
                        )
                )
        );
        form.attachTo(window.panel());
        new TerminalButton("Transfer money", () -> onTransfer(gui, form)).attachTo(window.panel());
        new TerminalButton("Cancel", this::onCancel).attachTo(window.panel());
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onCancel() {
        changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
    }

    private void onTransfer(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = serverBridge.execute(new TransferRequest(token, form.json()));
        if (resp.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", resp.message(), MessageDialogButton.OK);
            changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", resp.message(), MessageDialogButton.Close);
        }
    }
}
