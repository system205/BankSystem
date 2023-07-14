package oop.course.implementations;

import oop.course.exceptions.AccountException;
import oop.course.exceptions.AuthorizationException;
import oop.course.exceptions.ConflictException;
import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.Process;
import oop.course.interfaces.Request;
import oop.course.interfaces.Response;
import oop.course.responses.BadRequestResponse;
import oop.course.responses.ConflictResponse;
import oop.course.responses.InternalErrorResponse;
import oop.course.responses.UnauthorizedResponse;

public class ErrorResponsesProcess implements Process {
    private final Process next;

    public ErrorResponsesProcess(Process next) {
        this.next = next;
    }

    @Override
    public Response act(Request request) {
        try {
            return next.act(request);
        } catch (MalformedDataException | AccountException e) {
            return new BadRequestResponse(e.getMessage());
        } catch (ConflictException e) {
            return new ConflictResponse(e.getMessage());
        } catch (AuthorizationException e) {
            return new UnauthorizedResponse("Bearer", "", e.getMessage());
        }
        catch (Exception e) {
            return new InternalErrorResponse();
        }

    }
}
