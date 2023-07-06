package oop.course.client.views;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import oop.course.client.actions.Action;
import oop.course.client.actions.ChangeSceneAction;
import oop.course.client.gui.*;
import oop.course.client.requests.LoginRequest;
import oop.course.client.requests.Request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class LoginView implements IView {
    private final Consumer<Action> actionConsumer;

    public LoginView(Consumer<Action> actionHandler) {
        actionConsumer = actionHandler;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem authentication");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));

        new TerminalText(
                "Welcome to the BankSystem client application!\nPlease, register or login into your existing account."
        ).attachTo(contentPanel);

        var username = new TerminalFormKeyValuePair("email", new TerminalInputPair(new TerminalText("Email"), new TerminalTextBox()));
        var password = new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"), new TerminalPasswordBox()));

        var form = new TerminalForm(List.of(username, password));

        username.attachTo(contentPanel);
        password.attachTo(contentPanel);

        new TerminalButton("Login", () -> {
            Request req = new LoginRequest(form);
            try (Socket client = new Socket("127.0.0.1", 6666);
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                req.send(out);
                out.println("EOF");
                var resp = in.lines().collect(Collectors.joining("\n"));
                if (resp.equals("Wrong credentials")) {
                    MessageDialog.showMessageDialog(gui, "Server Response", resp, MessageDialogButton.Continue);
                }
                else {
                    MessageDialog.showMessageDialog(gui, "Token", resp, MessageDialogButton.OK);
                    window.close();
                    actionConsumer.accept(new ChangeSceneAction(Type.ActionSelect));
                }
            } catch (Exception e) {
                MessageDialog.showMessageDialog(gui, "Fatal error", "Unfortunately, the problem occurred when trying to communicate with the server", MessageDialogButton.OK);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Register page", () -> {
            window.close();
            actionConsumer.accept(new ChangeSceneAction(Type.Register));
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            actionConsumer.accept(new ChangeSceneAction(Type.None));
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }
}
