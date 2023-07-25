package oop.course.responses;

import org.slf4j.*;

import java.io.*;
import java.util.*;

public class ConflictResponse implements Response {
    private static final Logger log = LoggerFactory.getLogger(ConflictResponse.class);
    private final String message;

    public ConflictResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintWriter out) throws IOException {
        log.info("Conflict Response:\n");
        new BaseResponse(
                409, "Conflict",
                Map.ofEntries(
                        Map.entry("Content-Type", "application/json")
                ),
                new ResponseMessage(message).json()
        ).print(out);
    }
}
