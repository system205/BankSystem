package oop.course.routes.manager.requests.methods;

import oop.course.entity.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import org.slf4j.*;

import java.sql.*;

public final class PostRequests implements ProcessMethod {
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
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        String status = form.stringField("status");
        long id = form.longField("id");

        new CustomerRequest(
            id,
            this.connection
        ).update(status);

        return new SuccessResponse(
            new ResponseMessage("Updated successfully").json()
        );
    }

    @Override
    public boolean accept(String method) {
        return "POST".equals(method);
    }
}
