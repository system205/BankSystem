package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.storage.*;
import oop.course.storage.interfaces.*;
import oop.course.tools.implementations.*;

public class LoginRoute implements Route {
    private final CheckCredentials check;

    public LoginRoute(CheckCredentials check) {
        this.check = check;
    }

    @Override
    public Response act(Request request) {
        // Retrieve credentials from request body and check their presence in database (any kind of validation)

        Credentials credentials = new SimpleCredentials(
                new JsonForm(request.body())
        );

        if (this.check.ok(credentials)) {
            // Successfully logged in
            return new TokenResponse(credentials, "TODO");
        } else {
            // Failure when validating credentials
            return new LoginFailure("Wrong credentials");
        }
    }

    @Override
    public boolean accept(String path) {
        return "/login".equals(path);
    }

}
