package oop.course.client;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Client
{
    private Terminal terminal;
    public Client()
    {
        try
        {
            terminal = new DefaultTerminalFactory().createTerminal();
        }
        catch (IOException exception)
        {
            System.out.println(exception.getMessage());
            System.exit(-1);
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(Files.walk(Paths.get(".")).filter(Files::isRegularFile).filter(p -> p.getFileName().toString().endsWith("java")).map(path -> {try {return Files.lines(path).filter(s -> s.trim().length() > 0 && !s.trim().startsWith("/") && !s.trim().startsWith("*")).count();} catch (IOException e) {throw new RuntimeException(e);}}).reduce(Long::sum).orElse(0L));
        new Client().run();
    }

    public void run()
    {
        try {
            View currentView = new LoginView(terminal);
            while (true)
            {
                var next = currentView.show();
                boolean close = false;
                switch (next)
                {
                    case AccountView -> currentView = new AccountView(terminal);
                    case TransferView -> currentView = new TransferView(terminal);
                    case NoView -> {
                        terminal.close();
                        close = true;
                    }
                    case LoginView -> currentView = new LoginView(terminal);
                    case RegisterView ->  currentView = new RegisterView(terminal);
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