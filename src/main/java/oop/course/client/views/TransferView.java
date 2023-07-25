package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

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

        var window = new TerminalWindow(
            "Money transfer",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            form,
            new TerminalButton("Transfer money", () -> onTransfer(gui, form)),
            new TerminalButton("Cancel", this::onCancel)
        );

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
