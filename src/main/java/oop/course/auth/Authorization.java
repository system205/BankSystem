package oop.course.auth;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.interfaces.Process;

import java.util.*;

public class Authorization implements Process {

    private final Optional<Process> next;

    public Authorization(Optional<Process> next) {
        this.next = next;
    }

    public Authorization() {
        this.next = Optional.empty();
    }


    @Override
    public Response act(Request request) {
        // Internal logic

        // Process next if OK so far
        if (this.next.isPresent()) {
            return this.next.get().act(request);
        } else { // Unreachable state (exception is supposed to appear)
            return new EmptyResponse();
        }
    }
}
