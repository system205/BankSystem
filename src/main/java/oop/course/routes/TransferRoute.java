package oop.course.routes;

import oop.course.interfaces.*;

public class TransferRoute implements Route {
    private final ProcessMethod[] next;

    public TransferRoute(ProcessMethod... processes) {
        this.next = processes;
    }

    @Override
    public Response act(Request request) {
        // fork depending on the http method
        String method = request.method();

        for (ProcessMethod process : next) {
            if (process.accept(method)) {
                return process.act(request);
            }
        }

        throw new RuntimeException("Unsupported method " + method);
    }


    @Override
    public boolean accept(String path) {
        return "/transfer".equals(path);
    }
}
