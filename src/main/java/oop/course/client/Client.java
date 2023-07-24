package oop.course.client;

import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.MessageDialog;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import oop.course.client.gui.*;
import oop.course.client.requests.LoginRequest;
import oop.course.client.views.IView;
import oop.course.client.views.LoginView;
import oop.course.client.views.RegisterView;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Client {
    private final GUI clientGui;
    private final ServerBridge serverBridge;

    public Client(GUI clientGui, ServerBridge serverBridge) {
        this.clientGui = clientGui;
        this.serverBridge = serverBridge;
    }

    public static void main(String[] args) throws IOException {
        try (var screen = new DefaultTerminalFactory().createScreen()) {
            screen.startScreen();
            var stateMap = new HashMap<String, IView>();
            var serverBridge = new SocketServerBridge(
                "127.0.0.1",
                6666
            );
            var gui = new MultiWindowTextGUI(screen);
            var loginForm = new TerminalForm(
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
            );
            var loginWindow = new TerminalWindow(
                "BankSystem login",
                new Panel(
                    new LinearLayout(Direction.VERTICAL)
                )
            );
            stateMap.put(
                "loginView",
                new LoginView(
                    gui,
                    loginWindow,
                    List.of(
                        new TerminalText(
                            "Welcome to the BankSystem client application!\nPlease, register or login into your existing account."
                        ),
                        loginForm,
                        new TerminalButton("Login", () -> {
                            var resp = serverBridge.execute(new LoginRequest(loginForm.json()));

                            if (resp.isSuccess()) {
                                loginWindow.close();
                                stateMap.get("accountsView").show(gui);
                            } else {
                                MessageDialog.showMessageDialog(gui, "Authentication error", resp.message(), MessageDialogButton.Close);
                            }
                        }),
                        new TerminalButton("Register page", () -> {
                            loginWindow.close();
                            stateMap.get("registerView").show(gui);
                        }),
                        new TerminalButton("Exit", () -> System.exit(0))
                    ),
                    new HashMap<>()
                )
            );
            var registerWindow = new TerminalWindow(
                "BankSystem registering",
                new Panel(
                    new LinearLayout(Direction.VERTICAL)
                )
            );
            stateMap.put(
                "registerView",
                new RegisterView(
                    gui,
                    registerWindow,
                    List.of(
                        new TerminalText("Please enter your data for registration."),
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
                                    "name",
                                    new TerminalInputPair(
                                        new TerminalText("Name"),
                                        new TerminalTextBox()
                                    )
                                ),
                                new TerminalFormKeyValuePair(
                                    "surname",
                                    new TerminalInputPair(
                                        new TerminalText("Surname"),
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
                        new TerminalButton("Register", () -> {}),
                        new TerminalButton("Back to login page", () -> {
                            registerWindow.close();
                            stateMap.get("loginView").show(gui);
                        }),
                        new TerminalButton("Exit", () -> System.exit(0))
                    ),
                    new HashMap<>()
                )
            );
            new Client(
                new TerminalGUI(
                    gui,
                    List.of(
                        stateMap.get("loginView"),
                        stateMap.get("registerView")
                    )
                ),
                serverBridge
            ).run();
        }
    }

    public void run() {
        clientGui.startLooping(serverBridge);
    }
}