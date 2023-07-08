package oop.course.responses;

import oop.course.interfaces.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;

public class BadRequestResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(BadRequestResponse.class);

    @Override
    public void print(PrintWriter out) throws IOException {
        log.error("Bad Request:\n\n");
        new BaseResponse(400, "Bad Request").print(out);
    }
}
