package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.*;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public class AutoPaymentView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String account;

    public AutoPaymentView(Consumer<IView> changeViewHandler, Runnable onExit, Function<Request, BasicResponse> requestHandler,
                           String token, String accountNumber) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.onExit = onExit;
        account = accountNumber;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Auto payment");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        var sender = new TerminalFormKeyValuePair("senderNumber", new TerminalInputPair(new TerminalText("From"),
                new TerminalImmutableTextBox(account)));
        var receiver = new TerminalFormKeyValuePair("receiverNumber", new TerminalInputPair(new TerminalText("To"),
                new TerminalTextBox()));
        var amount = new TerminalFormKeyValuePair("amount", new TerminalInputPair(new TerminalText("Amount"),
                new TerminalTextBox()));
        var period = new TerminalFormKeyValuePair("period", new TerminalInputPair(new TerminalText("Period"),
                new TerminalTextBox()));
        var paymentId = new TerminalFormKeyValuePair("paymentId",
                new TerminalInputPair(new TerminalText("Payment ID"), new TerminalTextBox()));

        sender.attachTo(contentPanel);
        receiver.attachTo(contentPanel);
        amount.attachTo(contentPanel);
        period.attachTo(contentPanel);
        paymentId.attachTo(contentPanel);

        new TerminalButton("Set up", () -> {

        }).attachTo(contentPanel);

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountActionsView(onChangeView, onExit, requestHandler, token, account));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
