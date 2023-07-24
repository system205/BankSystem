package oop.course.client;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import oop.course.client.gui.*;
import oop.course.client.views.LoginView;

import java.io.IOException;
import java.util.List;

public class Client {
    private final GUI clientGui;
    private final ServerBridge serverBridge;

    public Client(GUI clientGui, ServerBridge serverBridge) {
        this.clientGui = clientGui;
        this.serverBridge = serverBridge;
    }

    public static void main(String[] args) throws IOException {
        new Client(
                new TerminalGUI(
                        List.of(
                                new LoginView(
                                        new TerminalWindow(
                                                "BankSystem login",
                                                new Panel(
                                                        new LinearLayout(Direction.VERTICAL)
                                                )
                                        ),
                                        List.of(
                                                new TerminalText(
                                                        "Welcome to the BankSystem client application!\nPlease, register or login into your existing account."
                                                ),
                                                new TerminalForm(
                                                        List.of(
                                                                new TerminalFormKeyValuePair(
                                                                        "email",
                                                                        new TerminalInputPair(
                                                                                new TerminalText("Email"),
                                                                                new TerminalTextBox()
                                                                        )
                                                                ),
                                                                new TerminalFormKeyValuePair(
                                                                        "password",
                                                                        new TerminalInputPair(
                                                                                new TerminalText("Password"),
                                                                                new TerminalPasswordBox()
                                                                        )
                                                                )
                                                        )
                                                ),
                                                new TerminalButton("Login", () -> {}),
                                                new TerminalButton("Register page", () -> {}),
                                                new TerminalButton("Exit", () -> {})
                                        )
                                )
                        )
                ),
                new SocketServerBridge(
                        "127.0.0.1",
                        6666
                )
        ).run();
    }

    public void run() throws IOException {
        clientGui.startLooping(serverBridge);
    }
}