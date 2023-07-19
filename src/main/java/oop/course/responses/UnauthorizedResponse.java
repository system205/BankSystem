package oop.course.responses;

import oop.course.interfaces.*;
import org.slf4j.*;

import java.io.*;
import java.util.Map;

import static java.util.Map.entry;

public class UnauthorizedResponse implements Response {
    /**
     * Should have the following structure:
     * HTTP/1.1 401 Unauthorized
     * WWW-Authenticate: Basic realm="realm"
     */

    public UnauthorizedResponse(String authScheme, String realm, String errorMessage) {
        this.authScheme = authScheme;
        this.realm = realm;
        this.errorMessage = errorMessage;
    }

    private final Logger log = LoggerFactory.getLogger(UnauthorizedResponse.class);
    private final String realm;
    private final String authScheme;
    private final String errorMessage;

    @Override
    public void print(PrintWriter out) throws IOException {
        log.info("Unauthorized Response: \n");
        final BaseResponse baseResponse = new BaseResponse(
                401,
                "Unauthorized",
                Map.ofEntries(
                        entry("WWW-Authenticate", authScheme + " realm=\"" + realm + "\""),
                        entry("Content-Type", "application/json")
                ),
                new ResponseMessage(errorMessage).json()
        );
        baseResponse.print(out);
//        String responseText =
//                "HTTP/1.1 401 Unauthorized\n" +
//                "WWW-Authenticate: " + authScheme + " realm=\"" + realm + "\"";
//        log.info("Sent response:\n\n" + responseText);
//        out.println(responseText);
    }
}