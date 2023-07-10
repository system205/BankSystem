package oop.course.implementations;

import oop.course.exceptions.MalformedDataException;
import oop.course.interfaces.*;
import oop.course.responses.InternalErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

// TODO - should I throw an exception if accessing well formatted headers, but at the same time th request line is malformed?
public class HttpRequest implements Request {
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    private final LinkedList<String> data;

    public HttpRequest(BufferedReader in) throws IOException {
        this.data = new LinkedList<>();
        String line;
        // TODO - change it to read until actual EOF
        while (!(line = in.readLine()).equals("EOF")) {
            this.data.add(line);
        }
        log.info("HttpRequest data:\n" + this.data);
    }

    private String[] requestLine() throws MalformedDataException {
        if (data.isEmpty()) {
            throw new MalformedDataException("HttpRequest is empty");
        }
        String[] requestLine = data.getFirst().split(" ");
        if (requestLine.length != 3) {
            throw new MalformedDataException("Request line is malformed: " + Arrays.toString(requestLine));
        }
        return requestLine;
    }

    @Override
    public Collection<String> headers() throws MalformedDataException {
        // a part of the data
        // TODO - multiple line headers are not implemented
        ArrayList<String> headers = new ArrayList<>();
        for (String line : this.data) {
            if (line.isEmpty()) break;
            headers.add(line);
        }
        headers.remove(0);
        for (String header : headers) {
            final String[] headerSplit = header.split(":");
            if (headerSplit.length != 2) {
                throw new MalformedDataException("Malformed header: " + header);
            }
        }
        return headers;
    }

    @Override
    public Iterable<String> body() {
        // a payload of the data
        Collection<String> payload = new LinkedList<>();
        boolean foundEmptyLine = false;
        for (String line : data) {
            if (line.isEmpty()) {
                foundEmptyLine = true;
                continue;
            }
            if (foundEmptyLine)
                payload.add(line);
        }
        return payload;
    }

    @Override
    public String url() throws MalformedDataException {
        return requestLine()[1]; // the very first word
    }

    @Override
    public String method() throws MalformedDataException {
        return requestLine()[0]; // the very first word
    }
}
