package oop.course.responses;

import oop.course.interfaces.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static java.util.Map.entry;

public class InternalErrorResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(InternalErrorResponse.class);
    private final String message;

    public InternalErrorResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintWriter out) throws IOException {
        log.error("Internal Error Response:\n");
        new BaseResponse(
                500,
                "Internal Server Error",
                Map.ofEntries(
                    entry("Content-Type", "application/json")
                ),
                new ResponseMessage(message).json()
        ).print(out);
    }
}
