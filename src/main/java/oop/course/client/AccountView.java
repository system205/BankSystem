package oop.course.client;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;

import java.util.function.Consumer;

public class AccountView implements IView {
    private final Consumer<Action> actionConsumer;

    public AccountView(Consumer<Action> actionHandler) {
        actionConsumer = actionHandler;
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
            actionConsumer.accept(new ChangeSceneAction(Type.Transfer));
        }).attachTo(contentPanel);

        new TerminalButton("Logout", () -> {
            window.close();
            actionConsumer.accept(new ChangeSceneAction(Type.Login));
        }).attachTo(contentPanel);

        new TerminalButton("Logout & exit", () -> {
            window.close();
            actionConsumer.accept(new ChangeSceneAction(Type.None));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
