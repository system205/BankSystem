package oop.course.implementations;

import oop.course.exceptions.*;
import oop.course.interfaces.Process;
import oop.course.interfaces.Request;
import oop.course.interfaces.Response;
import oop.course.responses.BadRequestResponse;
import oop.course.responses.ConflictResponse;
import oop.course.responses.InternalErrorResponse;
import oop.course.responses.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorResponsesProcess implements Process {
    private static final Logger logger = LoggerFactory.getLogger(ErrorResponsesProcess.class);
    private final Process next;

    public ErrorResponsesProcess(Process next) {
        this.next = next;
    }

    @Override
    public Response act(Request request) {
        try {
            return next.act(request);
        } catch (MalformedDataException | AccountException | IllegalStateException e) {
            return new BadRequestResponse(e.getMessage());
        } catch (ConflictException e) {
            return new ConflictResponse(e.getMessage());
        } catch (AuthorizationException e) {
            return new UnauthorizedResponse("Bearer", "", e.getMessage());
        } catch (ForbiddenException e) {
            return new ForbiddenResponse(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            return new InternalErrorResponse();
        }

    }
}
