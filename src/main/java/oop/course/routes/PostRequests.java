package oop.course.routes;

import oop.course.entity.*;
import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.responses.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;
import org.slf4j.*;

import java.sql.*;

public class PostRequests implements ProcessMethod {
    private static final Logger log = LoggerFactory.getLogger(PostRequests.class);
    private final Connection connection;

    public PostRequests(Connection connection) {
        this.connection = connection;
        log.trace("New Post request");
    }

    /**
     * Takes id and new type (approved or denied) of a request
     */
    @Override
    public Response act(Request request) throws MalformedDataException {
        Form form = new JsonForm(request.body());
        String status = form.stringField("status");
        long id = form.longField("id");
        new CustomerRequest(
                id,
                this.connection
        ).update(status);
        return new SuccessResponse("Updated successfully");
    }

    @Override
    public boolean accept(String method) {
        return "POST".equals(method);
    }
}
