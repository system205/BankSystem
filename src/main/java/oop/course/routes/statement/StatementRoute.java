package oop.course.routes.statement;

import oop.course.entity.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;
import oop.course.miscellaneous.implementations.*;
import oop.course.miscellaneous.interfaces.*;

import java.sql.*;
import java.time.*;
import java.time.format.*;

public class StatementRoute implements Route {
    private final Connection connection;

    public StatementRoute(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Response act(Request request) throws Exception {
        Form form = new JsonForm(request.body());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new SuccessResponse(
                new Customer(
                        connection,
                        new HeaderToken(request.headers()).id()
                ).account(form.stringField("accountNumber"))
                        .compose(
                                LocalDate.parse(form.stringField("startDate"), formatter),
                                LocalDate.parse(form.stringField("endDate"), formatter)
                        ).json()
        );
    }

    @Override
    public boolean accept(String path) {
        return "/stats".equals(path);
    }
}
