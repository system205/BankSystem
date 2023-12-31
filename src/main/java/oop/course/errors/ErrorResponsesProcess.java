package oop.course.errors;

import oop.course.errors.exceptions.*;
import oop.course.requests.*;
import oop.course.responses.*;
import oop.course.routes.Process;
import org.slf4j.*;

public final class ErrorResponsesProcess implements Process {
    private static final Logger logger = LoggerFactory.getLogger(ErrorResponsesProcess.class);
    private final Process next;

    public ErrorResponsesProcess(Process next) {
        this.next = next;
    }

    @Override
    public Response act(Request request) throws Exception {
        try {
            return next.act(request);
        } catch (NotFoundException e) {
            logger.error(e.getMessage(), e);
            return new NotFoundResponse();
        } catch (MalformedDataException | AccountException | IllegalStateException e) {
            logger.error(e.getMessage(), e);
            return new BadRequestResponse(e.getMessage());
        } catch (ConflictException e) {
            logger.error(e.getMessage(), e);
            return new ConflictResponse(e.getMessage());
        } catch (AuthorizationException e) {
            logger.error(e.getMessage(), e);
            return new UnauthorizedResponse("Bearer", e.realm(), e.getMessage());
        } catch (ForbiddenException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            return new ForbiddenResponse(e.getMessage());
        } catch (MethodNotAllowedException e) {
            logger.error(e.getMessage(), e);
            return new MethodNotAllowedResponse();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new InternalErrorResponse(e.getMessage());
        }
    }
}
