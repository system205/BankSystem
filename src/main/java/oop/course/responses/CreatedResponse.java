package oop.course.responses;

import org.slf4j.*;

import java.io.*;
import java.util.*;

public final class CreatedResponse implements Response {
    private static final Logger log = LoggerFactory.getLogger(CreatedResponse.class);
    private final String message;

    public CreatedResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintWriter out) throws IOException {
        log.info("Created Response:\n");
        new BaseResponse(
            201, "Created",
            Map.ofEntries(
                Map.entry("Content-Type", "application/json")
            ),
            new ResponseMessage(message).json()
        ).print(out);
    }
}
