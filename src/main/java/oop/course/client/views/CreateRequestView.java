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

public class CreateRequestView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final ServerBridge serverBridge;
    private final String token;
    private final String account;
    private final TerminalWindow window;
    private final Panel contentPanel;

    public CreateRequestView(Consumer<IView> changeViewHandler, Runnable onExit, ServerBridge serverBridge,
                             String token, String accountNumber) {
        this.onChangeView = changeViewHandler;
        this.serverBridge = serverBridge;
        this.token = token;
        this.onExit = onExit;
        this.account = accountNumber;
        this.contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Create request", contentPanel);
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        var form = new TerminalForm(
                List.of(
                        new TerminalFormKeyValuePair(
                                "accountNumber",
                                new TerminalInputPair(
                                        new TerminalText("Account number"),
                                        new TerminalImmutableTextBox(account)
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
        form.attachTo(contentPanel);
        new TerminalButton("Create", () -> onCreate(gui, form)).attachTo(contentPanel);
        new TerminalButton("Cancel", this::onCancel).attachTo(contentPanel);
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    private void onCreate(WindowBasedTextGUI gui, TerminalForm form) {
        var response = serverBridge.execute(new CreateRequestRequest(token, form.json()));
        if (response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Success", response.message(), MessageDialogButton.OK);
            onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
        } else {
            MessageDialog.showMessageDialog(gui, "Failure", response.message(), MessageDialogButton.Close);
        }
    }

    private void onCancel() {
        onChangeView.accept(new AccountsView(onChangeView, onExit, serverBridge, token));
    }
}
