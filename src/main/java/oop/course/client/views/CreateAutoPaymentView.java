package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.NewAutoPaymentRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.NewAutoPaymentResponse;
import oop.course.client.responses.BasicResponse;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CreateAutoPaymentView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String account;

    public CreateAutoPaymentView(Consumer<IView> changeViewHandler, Runnable onExit,
                                 Function<Request, BasicResponse> requestHandler, String token, String accountNumber) {
        this.onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.onExit = onExit;
        this.account = accountNumber;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        TerminalWindow window = new TerminalWindow("Auto payment", contentPanel);

        var form = new TerminalForm(List.of(
                new TerminalFormKeyValuePair("senderNumber",
                        new TerminalInputPair(
                                new TerminalText("From"),
                                new TerminalImmutableTextBox(account)
                        )
                ),
                new TerminalFormKeyValuePair("receiverNumber",
                        new TerminalInputPair(
                                new TerminalText("To"),
                                new TerminalTextBox()
                        )
                ),
                new TerminalFormKeyValuePair("amount",
                        new TerminalInputPair(
                                new TerminalText("Amount"),
                                new TerminalTextBox()
                        )
                ),
                new TerminalFormKeyValuePair("period",
                        new TerminalInputPair(
                                new TerminalText("Period"),
                                new TerminalTextBox()
                        )
                ),
                new TerminalFormKeyValuePair("startDate",
                        new TerminalInputPair(
                                new TerminalText("Starting date"),
                                new TerminalTextBox()
                        )
                ))
        );
        form.attachTo(contentPanel);


        new TerminalButton("Set up", () -> {
            var request = new NewAutoPaymentRequest(token, form);
            var response = new NewAutoPaymentResponse(requestHandler.apply(request));
            if (response.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "The auto-payment was successfully set up",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new AccountActionsView(onChangeView, onExit, requestHandler, token, account));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "The auto-payment could not be set up",
                        MessageDialogButton.Close);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountActionsView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        window.addToGui(gui);
        window.open();
    }
}
