package oop.course.responses;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class BadRequestResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(BadRequestResponse.class);

    final String message;

    public BadRequestResponse() {
        this("");
    }

    public BadRequestResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintWriter out) throws IOException {
        log.error("Bad Request:\n\n");
        new BaseResponse(
                400, "Bad Request",
                Map.ofEntries(
                        Map.entry("Content-Type", "application/json")
                ),
                new ResponseMessage(message).json()
        ).print(out);
    }
}
