package oop.course.client.views;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.*;
import oop.course.client.*;
import oop.course.client.gui.*;
import oop.course.client.requests.*;

import java.util.*;
import java.util.function.*;

public final class CreateAutoPaymentView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String account;

    public CreateAutoPaymentView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
                                 String token, String accountNumber) {
        this.changeView = changeView;
        this.serverBridge = serverBridge;
        this.token = token;
        this.exitAction = exitAction;
        this.account = accountNumber;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var form = new TerminalForm(
            List.of(
                new TerminalFormKeyValuePair(
                    "senderNumber",
                    new TerminalInputPair(
                        new TerminalText("From"),
                        new TerminalFixedTextBox(account)
                    )
                ),
                new TerminalFormKeyValuePair(
                    "receiverNumber",
                    new TerminalInputPair(
                        new TerminalText("To"),
                        new TerminalTextBox()
                    )
                ),
                new TerminalFormKeyValuePair(
                    "amount",
                    new TerminalInputPair(
                        new TerminalText("Amount"),
                        new TerminalTextBox()
                    )
                ),
                new TerminalFormKeyValuePair(
                    "period",
                    new TerminalInputPair(
                        new TerminalText("Period"),
                        new TerminalTextBox()
                    )
                ),
                new TerminalFormKeyValuePair(
                    "startDate",
                    new TerminalInputPair(
                        new TerminalText("Starting date"),
                        new TerminalTextBox())
                )
            )
        );

        var window = new TerminalWindow(
            "Auto payment",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            form,
            new TerminalButton("Set up", () -> onAutoPaymentSetup(gui, form)),
            new TerminalButton("Return", this::onReturn)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onReturn() {
        changeView.accept(new AccountActionsView(changeView, exitAction, serverBridge, token, account));
    }

    private void onAutoPaymentSetup(WindowBasedTextGUI gui, TerminalForm form) {
        var response = serverBridge.execute(new NewAutoPaymentRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            changeView.accept(new AccountActionsView(changeView, exitAction, serverBridge, token, account));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }
}
