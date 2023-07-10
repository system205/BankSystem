package oop.course.implementations;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.Process;
import oop.course.interfaces.Request;
import oop.course.interfaces.Response;
import oop.course.responses.BadRequestResponse;
import oop.course.responses.InternalErrorResponse;

import java.util.Optional;

public class ErrorResponsesProcess implements Process {
    private final Process next;

    public ErrorResponsesProcess(Process next) {
        this.next = next;
    }

    @Override
    public Response act(Request request) {
        try {
            return next.act(request);
        } catch (MalformedDataException e) {
            return new BadRequestResponse();
        } catch (Exception e) {
            return new InternalErrorResponse();
        }

    }
}
