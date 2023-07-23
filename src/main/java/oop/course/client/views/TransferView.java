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

public class TransferView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final String accountNumber;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public TransferView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge, String token,
                        String accountNumber) {
        onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.accountNumber = accountNumber;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Money transfer", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "senderAccount",
                                new TerminalInputPair(
                                        new TerminalText("Sender"),
                                        new TerminalImmutableTextBox(accountNumber)
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
        form.attachTo(contentPanel);
        new TerminalButton("Transfer money", () -> onTransfer(gui, form)).attachTo(contentPanel);
        new TerminalButton("Cancel", this::onCancel).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onCancel() {
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }

    private void onTransfer(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = serverBridge.execute(new TransferRequest(token, form.json()));
        if (resp.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", resp.message(), MessageDialogButton.OK);
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", resp.message(), MessageDialogButton.Close);
        }
    }
}
