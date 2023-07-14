package oop.course.routes;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.responses.MethodNotAllowedResponse;

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

        return new MethodNotAllowedResponse();
    }


    @Override
    public boolean accept(String path) {
        return "/transfer".equals(path);
    }
}
