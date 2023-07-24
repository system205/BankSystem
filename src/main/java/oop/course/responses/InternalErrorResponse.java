package oop.course.responses;

import org.slf4j.*;

import java.io.*;
import java.util.*;

public class InternalErrorResponse implements Response {
    private static final Logger log = LoggerFactory.getLogger(InternalErrorResponse.class);
    private final String message;

    public InternalErrorResponse(String message) {
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
