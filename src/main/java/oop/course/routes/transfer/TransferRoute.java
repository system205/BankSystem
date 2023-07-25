package oop.course.routes.transfer;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.*;

public final class TransferRoute implements Route {
    private final ProcessMethod[] next;

    public TransferRoute(ProcessMethod... processes) {
        this.next = processes;
    }

    @Override
    public Response act(Request request) throws Exception {
        String method = request.method();
        for (ProcessMethod process : next) {
            if (process.accept(method)) {
                return process.act(request);
            }
        }
        throw new MethodNotAllowedException("Method not supported in /transfer");
    }


    @Override
    public boolean accept(String path) {
        return "/transfer".equals(path);
    }
}
