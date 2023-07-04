package oop.course.client;

import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class LoginView implements IView {
    private BiConsumer<Type, String> onSceneChange;

    public LoginView() throws IOException {
        onSceneChange = (Type type, String token) -> {};
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
            RequestBuilder builder = new RequestBuilder();
            HttpJsonRequest req = builder.withPost().withJson(form.json()).withRoute("/login").build();
            try (Socket client = new Socket("127.0.0.1", 6666);
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                req.send(out);
                var resp = in.lines().collect(Collectors.joining("\n"));
                if (resp.equals("Wrong credentials")) {
                    MessageDialog.showMessageDialog(gui, "Server Response", resp, MessageDialogButton.Continue);
                }
                else {
                    MessageDialog.showMessageDialog(gui, "Token", resp, MessageDialogButton.OK);
                    window.close();
                    onSceneChange.accept(Type.ActionSelect, "");
                }
            } catch (Exception e) {
                MessageDialog.showMessageDialog(gui, "Fatal error", "Unfortunately, the problem occurred when trying to communicate with the server", MessageDialogButton.OK);
            }
        }).attachTo(contentPanel);

        new TerminalButton("Register page", () -> {
            window.close();
            onSceneChange.accept(Type.Register, "");
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onSceneChange.accept(Type.None, "");
        }).attachTo(contentPanel);

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    @Override
    public void registerChangeViewHandler(BiConsumer<Type, String> consumer) {
        onSceneChange = consumer;
    }
}
