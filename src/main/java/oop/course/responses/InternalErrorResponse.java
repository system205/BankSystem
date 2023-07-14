package oop.course.responses;

import oop.course.interfaces.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class InternalErrorResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(InternalErrorResponse.class);

    @Override
    public void print(PrintWriter out) throws IOException {
        log.error("Internal Error Response:\n");
        new BaseResponse(500, "Internal Server Error").print(out);
    }
}