package oop.course.client;

import com.googlecode.lanterna.gui2.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class AccountView implements IView {
    private BiConsumer<Type, String> onSceneChange;
    private final String token;

    public AccountView(String token) throws IOException {
        onSceneChange = (Type type, String string) -> {};
        this.token = token;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        TerminalWindow window = new TerminalWindow("Account");
        Panel contentPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        RequestBuilder builder = new RequestBuilder();
        HttpJsonRequest req = builder.withPost().withRoute("/accounts").withToken(token).build();
        try (Socket client = new Socket("127.0.0.1", 6666);
             PrintWriter out = new PrintWriter(client.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
            req.send(out);
            var resp = in.lines().collect(Collectors.joining("\n"));
            new TerminalText(resp).attachTo(contentPanel);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        /*new TerminalText("Welcome to your account.").attachTo(contentPanel);
        new TerminalText("Name:").attachTo(contentPanel);
        new TerminalText("Ivan Ivanov").attachTo(contentPanel);
        new TerminalText("Account number:").attachTo(contentPanel);
        new TerminalText("123456").attachTo(contentPanel);
        new TerminalText("Balance").attachTo(contentPanel);
        new TerminalText("120.0$").attachTo(contentPanel);*/

        new TerminalButton("Transfer money", () -> {
            window.close();
            onSceneChange.accept(Type.Transfer, "");
        }).attachTo(contentPanel);

        new TerminalButton("Logout", () -> {
            window.close();
            onSceneChange.accept(Type.Login, "");
        }).attachTo(contentPanel);

        new TerminalButton("Logout & exit", () -> {
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
