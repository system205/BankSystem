package oop.course.routes.admin.applicants.methods;

import oop.course.entity.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

public final class ListApplicants implements ProcessMethod {
    private final Admin admin;

    public ListApplicants(Admin admin) {
        this.admin = admin;
    }

    @Override
    public Response act(Request request) throws Exception {
        return new SuccessResponse(this.admin.offers());
    }

    @Override
    public boolean accept(String method) {
        return "GET".equals(method);
    }
}
