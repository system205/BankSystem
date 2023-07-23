package oop.course.implementations;

import oop.course.interfaces.Response;
import oop.course.responses.BaseResponse;
import oop.course.responses.InternalErrorResponse;
import oop.course.responses.ResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ForbiddenResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(InternalErrorResponse.class);
    final String message;

    public ForbiddenResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintWriter out) throws IOException {
        log.error("Internal Error Response:\n");
        new BaseResponse(
                500, "Internal Server Error",
                Map.ofEntries(
                        Map.entry("Content-Type", "application/json")
                ),
                new ResponseMessage(message).json()
        ).print(out);
    }
}
