package oop.course.client;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class TerminalGUI implements GUI{
    Terminal terminal;
    public TerminalGUI() throws IOException {
        terminal = new DefaultTerminalFactory().createTerminal();
    }

    @Override
    public void startLooping() {
        try {
            View currentView = new LoginView(terminal);
            while (true)
            {
                var next = currentView.show();
                boolean close = false;
                switch (next)
                {
                    case Account -> currentView = new AccountView(terminal);
                    case Transfer -> currentView = new TransferView(terminal);
                    case None -> {
                        terminal.close();
                        close = true;
                    }
                    case Login -> currentView = new LoginView(terminal);
                    case Register ->  currentView = new RegisterView(terminal);
                }
                if (close) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}
