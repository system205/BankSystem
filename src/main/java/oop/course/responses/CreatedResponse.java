package oop.course.responses;

import oop.course.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;

import static java.util.Map.entry;

public class CreatedResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(CreatedResponse.class);
    private final String message;

    public CreatedResponse(String message) {
        this.message = message;
    }

    @Override
    public void print(PrintWriter out) throws IOException {
        log.info("Created Response:\n");
        new BaseResponse(201, "Created",
                Map.ofEntries(
                    entry("Content-Type", "application/json")
                ),
                new ResponseMessage(message).json()
        ).print(out);
    }
}
