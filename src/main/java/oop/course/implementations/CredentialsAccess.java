package oop.course.implementations;


import oop.course.interfaces.*;
import oop.course.routes.Process;
import oop.course.requests.Request;
import oop.course.responses.LoginFailure;
import oop.course.responses.Response;
import oop.course.storage.*;
import oop.course.tools.implementations.*;

public class CredentialsAccess implements Process {
    private final Process next;
    private final CheckCredentials check;

    public CredentialsAccess(CheckCredentials check, Process next) {
        this.check = check;
        this.next = next;
    }

    @Override
    public Response act(Request request) throws Exception {
        if (this.check.ok(new SimpleCredentials(
                new JsonForm(request.body())
        ))) {
            // Successfully authorized - proceed
            return this.next.act(request);
        } else {
            // Failure when validating credentials
            return new LoginFailure("Wrong credentials");
        }
    }
}
