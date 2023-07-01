package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;

public class RegisterRoute implements Route {
    @Override
    public Response act(Request request) {
        // provide all the necessary information to register new personal account
        return new EmptyResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/register".equals(path);
    }
}
