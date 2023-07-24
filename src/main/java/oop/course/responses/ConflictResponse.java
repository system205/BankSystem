package oop.course.responses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class ConflictResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(ConflictResponse.class);
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
