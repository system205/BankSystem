package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.ServerBridge;
import oop.course.client.gui.*;
import oop.course.client.requests.CreateRequestRequest;

import java.util.List;
import java.util.function.Consumer;

public final class CreateRequestView implements IView {
    private final Consumer<IView> changeView;
    private final Runnable exitAction;
    private final ServerBridge serverBridge;
    private final String token;
    private final String account;

    public CreateRequestView(Consumer<IView> changeView, Runnable exitAction, ServerBridge serverBridge,
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
                                "accountNumber",
                                new TerminalInputPair(
                                        new TerminalText("Account number"),
                                        new TerminalFixedTextBox(account)
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "type",
                                new TerminalInputPair(
                                        new TerminalText("Type"),
                                        new DropDownTextBox(List.of("deposit", "withdraw"))
                                )
                        ),
                        new TerminalFormKeyValuePair(
                                "amount",
                                new TerminalInputPair(
                                        new TerminalText("Amount"),
                                        new TerminalTextBox()
                                )
                        )
                )
        );

        var window = new TerminalWindow(
            "Create request",
            new Panel(new LinearLayout(Direction.VERTICAL)),
            new TerminalButton("Create", () -> onCreate(gui, form)),
            new TerminalButton("Cancel", this::onCancel)
        );

        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onCreate(WindowBasedTextGUI gui, TerminalForm form) {
        var response = serverBridge.execute(new CreateRequestRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }

    private void onCancel() {
        changeView.accept(new AccountsView(changeView, exitAction, serverBridge, token));
    }
}
