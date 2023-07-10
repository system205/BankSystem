package oop.course.routes;

import oop.course.entity.Customer;
import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.Process;
import oop.course.interfaces.*;
import oop.course.responses.UnauthorizedResponse;
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
    public Response act(Request request) throws MalformedDataException {
        Form form = new JsonForm(request.body());
        Customer customer = new Customer(connection, form);
        if (!customer.exists()) {
            log.error("Customer not found:\nEmail: " +
                    form.stringField("email") +
                    "\nPassword: " +
                    form.stringField("password")
            );
            return new UnauthorizedResponse("Bearer", "/login", "No such customer");
        }
        if (!customer.correctCredentials(form.stringField("password"))) {
            log.info("Invalid credentials:\nEmail: " +
                    form.stringField("email") +
                    "\nPassword: " +
                    form.stringField("password")
            );
            return new UnauthorizedResponse("Bearer", "/login", "Wrong password");
        }
        return next.act(request);
    }

    @Override
    public boolean accept(String path) {
        return "/login".equals(path);
    }

}
