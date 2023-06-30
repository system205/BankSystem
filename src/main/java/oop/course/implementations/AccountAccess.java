package oop.course.implementations;

import oop.course.interfaces.Process;
import oop.course.interfaces.*;

public class AccountAccess implements Process {
    private final Process next;

    public AccountAccess(Process next) {
        this.next = next;
    }

    @Override
    public Response act(Request request) {
//        if (!this.db.accept(
//                new HeaderToken(request.headers())
//        )) {
//            return new IllegalAccessResponse("Can't check account since unauthorized.");
//        }
        return this.next.act(request);
    }
}
