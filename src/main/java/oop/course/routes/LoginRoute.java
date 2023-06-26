package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;

public class LoginRoute implements Route {
    @Override
    public Response act(Request request) {
        // Retrieve credentials from request body and check their presence in database

        // Return either JWT token if logged successfully
        // or UserNotFound response
        // or BadCredentials if password is wrong
        
        return new EmptyResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/login".equals(path);
    }

}
