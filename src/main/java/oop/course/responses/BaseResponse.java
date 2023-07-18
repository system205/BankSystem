package oop.course.responses;

import oop.course.interfaces.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class BaseResponse implements Response {
    private final Logger log = LoggerFactory.getLogger(BaseResponse.class);
    private final int statusCode;
    private final String name;
    private final Map<String, Object> headers;
    private final String body;

    public BaseResponse(int statusCode, String name, Map<String, Object> headers, String body) {
        this.statusCode = statusCode;
        this.name = name;
        this.headers = headers;
        this.body = body;
    }

    public BaseResponse(int statusCode, String name) {
        this(statusCode, name, new HashMap<>(), "");
    }

    public BaseResponse(int statusCode, String name, Map<String, Object> headers) {
        this(statusCode, name, headers, "");
    }

    @Override
    public void print(PrintWriter out) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ");
        sb.append(statusCode);
        sb.append(" ").append(name);
        sb.append("\n");

        if (!headers.isEmpty()) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                sb.append(entry.getKey()).append(": ").append(entry.getValue());
            }
            sb.append("\n");
        }
        sb.append("\n");
        if (!body.isEmpty()) {
            sb.append(body);
            sb.append("\n");
        }
        sb.append("EOF");
        log.info("Sent response:\n{}", sb);
        out.println(sb);
    }
}
