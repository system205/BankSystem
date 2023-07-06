package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.TerminalButton;
import oop.course.client.gui.TerminalText;
import oop.course.client.gui.TerminalWindow;
import oop.course.client.requests.Request;
import oop.course.client.responses.BasicResponse;

import java.util.function.Consumer;
import java.util.function.Function;

public class AccountView implements IView {
    private final Consumer<Type> onChangeView;
    private final Function<Request, BasicResponse> requestHandler;

    public AccountView(Consumer<Type> changeViewHandler, Function<Request, BasicResponse> requestHandler) {
        onChangeView = changeViewHandler;
        this.requestHandler = requestHandler;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Account");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        /*Request req = new AuthorizedRequest(
                new BasicHttpRequest(Request.Method.POST, "/accounts"),
                token
        );
        try (Socket client = new Socket("127.0.0.1", 6666);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            req.send(out);
            out.println("EOF");
            var resp = in.lines().collect(Collectors.joining("\n"));
            MessageDialog.showMessageDialog(gui, "response", resp, MessageDialogButton.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


        new TerminalText("Welcome to your account.").attachTo(contentPanel);
        new TerminalText("Name:").attachTo(contentPanel);
        new TerminalText("Ivan Ivanov").attachTo(contentPanel);
        new TerminalText("Account number:").attachTo(contentPanel);
        new TerminalText("123456").attachTo(contentPanel);
        new TerminalText("Balance").attachTo(contentPanel);
        new TerminalText("120.0$").attachTo(contentPanel);

        new TerminalButton("Transfer money", () -> {
            window.close();
            onChangeView.accept(Type.Transfer);
        }).attachTo(contentPanel);

        new TerminalButton("Logout", () -> {
            window.close();
            onChangeView.accept(Type.Login);
        }).attachTo(contentPanel);

        new TerminalButton("Logout & exit", () -> {
            window.close();
            onChangeView.accept(Type.None);
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
