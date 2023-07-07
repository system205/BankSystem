package oop.course.responses;

import oop.course.interfaces.*;
import org.slf4j.*;

import java.io.*;

public class UnauthorizedResponse implements Response {
    /**
     * Should have the following structure:
     * HTTP/1.1 401 Unauthorized
     * WWW-Authenticate: Basic realm="realm"
     */

    public UnauthorizedResponse(String authScheme, String realm) {
        this.authScheme = authScheme;
        this.realm = realm;
    }

    private final Logger log = LoggerFactory.getLogger(UnauthorizedResponse.class);
    private final String realm;
    private final String authScheme;

    @Override
    public void print(PrintWriter out) throws IOException {
        String responseText =
                "HTTP/1.1 401 Unauthorized\n" +
                "WWW-Authenticate: " + authScheme + " realm=\"" + realm + "\"";
        log.info("Sent response:\n\n" + responseText);
        out.println(responseText);
    }
}
