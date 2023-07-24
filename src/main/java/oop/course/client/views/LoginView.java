package oop.course.client.views;

import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import oop.course.client.gui.TerminalGUIElement;
import oop.course.client.gui.TerminalWindow;

import java.util.List;

public class LoginView implements IView {
    private final TerminalWindow window;
    private final List<TerminalGUIElement> elements;

    public LoginView(TerminalWindow window, List<TerminalGUIElement> elements) {
        this.window = window;
        this.elements = elements;
    }

    @Override
    public void show(WindowBasedTextGUI gui) {
        for (var element : elements) {
            element.attachTo(window.panel());
        }
        window.addToGui(gui);
        window.open();
        window.waitUntilClosed();
    }

    /*private void onLogin(WindowBasedTextGUI gui, TerminalForm form) {
        var resp = this.serverBridge.execute(new LoginRequest(form.json()));

        if (resp.isSuccess()) {
            this.onChangeView.accept(new AccountsView(this.onChangeView, this.onExit, this.serverBridge, resp.token()));
        } else {
            MessageDialog.showMessageDialog(gui, "Authentication error", resp.message(), MessageDialogButton.Close);
        }
    }

    private void onRegister() {
        this.onChangeView.accept(new RegisterView(this.onChangeView, this.onExit, this.serverBridge));
    }

    private void onExit() {
        this.onExit.run();
    }*/
}
