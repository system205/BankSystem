package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.GetOffersRequest;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;
import oop.course.client.responses.GetOffersResponse;

import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;

public class OfferManagementView implements IView {
    private final Consumer<IView> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;
    private final String token;

    public OfferManagementView(Consumer<IView> changeViewHandler, Function<Request, BasicResponse> requestHandler, String token) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
        this.token = token;
    }
    @Override
    public void show(WindowBasedTextGUI gui) throws IOException {
        TerminalWindow window = new TerminalWindow("Account selection");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        Request req = new GetOffersRequest(token);
        var response = new GetOffersResponse(requestHandler.apply(req));

        //TODO: display the offers and handle approving/denying once the backend is updated

        if (!response.isSuccess()) {
            MessageDialog.showMessageDialog(gui, "Operation failed", "You do not have access or the server did not respond", MessageDialogButton.Abort);
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token));
            return;
        }

        new TerminalButton("Return", () -> {
            window.close();
            onChangeView.accept(new AccountsView(onChangeView, requestHandler, token));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
