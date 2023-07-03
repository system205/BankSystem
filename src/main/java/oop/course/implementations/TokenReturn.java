package oop.course.implementations;

import oop.course.interfaces.*;
import oop.course.interfaces.Process;
import oop.course.tools.implementations.*;

public class TokenReturn implements Process {
    private final String secretKey;

    public TokenReturn(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public Response act(Request request) {
        return new TokenResponse(
                new JsonForm(request.body())
                        .stringField("email"),
                this.secretKey);
    }
}
