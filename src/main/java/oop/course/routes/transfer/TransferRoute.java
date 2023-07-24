package oop.course.routes.transfer;

import oop.course.errors.exceptions.MethodNotAllowedException;
import oop.course.requests.Request;
import oop.course.responses.Response;
import oop.course.routes.ProcessMethod;
import oop.course.routes.Route;

public class TransferRoute implements Route {
    private final ProcessMethod[] next;

    public TransferRoute(ProcessMethod... processes) {
        this.next = processes;
    }

    @Override
    public Response act(Request request) throws Exception {
        // fork depending on the http method
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
