package oop.course.routes.login;

import oop.course.entity.Customer;
import oop.course.errors.exceptions.AuthorizationException;
import oop.course.routes.Process;
import oop.course.requests.Request;
import oop.course.responses.Response;
import oop.course.routes.Route;
import oop.course.tools.implementations.JsonForm;
import oop.course.tools.interfaces.Form;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class LoginRoute implements Route {
    private final Process next;
    private final Connection connection;
    private final Logger log = LoggerFactory.getLogger(LoginRoute.class);

    public LoginRoute(Connection connection, Process next) {
        this.connection = connection;
        this.next = next;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        Customer customer = new Customer(connection, form);
        if (!customer.exists()) {
            log.error("Customer not found");
            throw new AuthorizationException("/login", "No such customer");
        }
        if (!customer.correctCredentials(form.stringField("password"))) {
            log.info(
                    "Invalid credentials:\nEmail: {}\nPassword: {}",
                    form.stringField("email"),
                    form.stringField("password")
            );
            throw new AuthorizationException("/login", "Wrong password");
        }
        return next.act(request);
    }

    @Override
    public boolean accept(String path) {
        return "/login".equals(path);
    }

}