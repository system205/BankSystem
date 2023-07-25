package oop.course.responses;

import org.slf4j.*;

import java.io.*;
import java.util.*;

public final class ForbiddenResponse implements Response {
    private static final Logger log = LoggerFactory.getLogger(ForbiddenResponse.class);
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
