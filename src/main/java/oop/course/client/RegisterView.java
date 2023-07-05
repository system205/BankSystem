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

public class RegisterView implements IView {
    private BiConsumer<Type, String> onSceneChange;


    public RegisterView() throws IOException {
        onSceneChange = (Type type, String string) -> {};
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("BankSystem registration");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        new TerminalText("Please enter your data for registration.").attachTo(contentPanel);
        var email = new TerminalFormKeyValuePair("email", new TerminalInputPair(new TerminalText("Email"), new TerminalTextBox()));
        var name = new TerminalFormKeyValuePair("name", new TerminalInputPair(new TerminalText("Name"), new TerminalTextBox()));
        var surname = new TerminalFormKeyValuePair("surname", new TerminalInputPair(new TerminalText("Surname"), new TerminalTextBox()));
        var passwordField1 = new TerminalPasswordBox();
        var passwordField2 = new TerminalPasswordBox();
        var password = new TerminalFormKeyValuePair("password", new TerminalInputPair(new TerminalText("Password"), passwordField1));
        var password2 = new TerminalInputPair(new TerminalText("Repeat password"), passwordField2);

        var form = new TerminalForm(List.of(email, name, surname, password));

        email.attachTo(contentPanel);
        name.attachTo(contentPanel);
        surname.attachTo(contentPanel);
        password.attachTo(contentPanel);
        password2.attachTo(contentPanel);

        new TerminalButton("Register", () -> {
            if (!passwordField1.text().equals(passwordField2.text())) {
                MessageDialog.showMessageDialog(gui, "Error", "Passwords do not match", MessageDialogButton.Close);
                return;
            }
            //Check all other requirements for the fields
            RequestBuilder builder = new RequestBuilder();
            HttpJsonRequest req = builder.withPost().withJson(form.json()).withRoute("/register").build();
            try (Socket client = new Socket("127.0.0.1", 6666);
                 PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                req.send(out);
                MessageDialog.showMessageDialog(gui, "Server Response", in.lines().collect(Collectors.joining("\n")), MessageDialogButton.OK);
                window.close();
                onSceneChange.accept(Type.Login, "");
            } catch (Exception e) {
                MessageDialog.showMessageDialog(gui, "Fatal error", "Unfortunately, the problem occurred when trying to communicate with the server", MessageDialogButton.OK);
            }
        }
        ).attachTo(contentPanel);
        new TerminalButton("Back to login page", () -> {
            window.close();
            onSceneChange.accept(Type.Login, "");
        }).attachTo(contentPanel);

        new TerminalButton("Exit", () -> {
            window.close();
            onSceneChange.accept(Type.None, "");
        });

        window.setContent(contentPanel);
        window.addToGui(gui);
        window.open();
    }

    @Override
    public void registerChangeViewHandler(BiConsumer<Type, String> consumer) {
        onSceneChange = consumer;
    }
}
