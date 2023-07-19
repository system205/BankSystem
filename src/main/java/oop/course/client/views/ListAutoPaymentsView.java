package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.*;
import oop.course.client.requests.DeleteAutoPaymentRequest;
import oop.course.client.requests.ListAutoPaymentsRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.DeleteAutoPaymentResponse;
import oop.course.client.responses.ListAutoPaymentsResponse;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ListAutoPaymentsView implements IView {
    private final Consumer<IView> onChangeView;
    private final Runnable onExit;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;
    private final String account;
    private final TerminalWindow window;
    private final Panel panel;

    public ListAutoPaymentsView(Consumer<IView> changeViewHandler, Runnable onExit,
                                Function<Request, BasicResponse> requestHandler, String token, String account) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
        this.onExit = onExit;
        this.account = account;
        this.panel = new Panel(new LinearLayout(Direction.VERTICAL));
        this.window = new TerminalWindow("Autopayments", panel);
    }


    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {

        var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("accountNumber",
                new TerminalInputPair(new TerminalText("Account number"), new TerminalImmutableTextBox(account)))));

        var request = new ListAutoPaymentsRequest(token, form);
        var response = new ListAutoPaymentsResponse(requestHandler.apply(request));

        if (response.isSuccess()) {
            new TerminalAutoPaymentsTable(response.autoPayments(), (List<String> row) -> onRowSelected(row, gui)).attachTo(panel);
        } else {
            new TerminalText("Could not fetch data from the server").attachTo(panel);
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, onExit, requestHandler, token));
        }).attachTo(panel);

        window.addToGui(gui);
        window.open();
    }

    private void onRowSelected(List<String> row, WindowBasedTextGUI gui) {
        var res = MessageDialog.showMessageDialog(gui, "Select an action", "Do you want to cancel the auto-payment?",
                MessageDialogButton.Yes, MessageDialogButton.No);
        if (res == MessageDialogButton.Yes) {
            var form = new TerminalForm(List.of(new TerminalFormKeyValuePair("paymentId",
                    new TerminalInputPair(new TerminalText("Payment Id"), new TerminalImmutableTextBox(row.get(0))))));
            var deleteRequest = new DeleteAutoPaymentRequest(token, form);
            var deleteResponse = new DeleteAutoPaymentResponse(requestHandler.apply(deleteRequest));
            if (deleteResponse.isSuccess()) {
                MessageDialog.showMessageDialog(gui, "Success", "Successfully canceled an auto-payment",
                        MessageDialogButton.OK);
                window.close();
                onChangeView.accept(new ListAutoPaymentsView(onChangeView, onExit, requestHandler, token, account));
            } else {
                MessageDialog.showMessageDialog(gui, "Failure", "Failed to cancel an auto-payment",
                        MessageDialogButton.Close);
            }
        }
    }
}
