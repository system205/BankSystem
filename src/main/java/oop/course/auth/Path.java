package oop.course.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Path {
    private final Collection<String> headers;

    private final Map<String, String> map = new HashMap<String, String>();
    private String[] meta;

    public Path(Collection<String> headers) {
        this.headers = headers;
        processHeaders();
    }

    private void processHeaders() {
        meta = headers.iterator().next().split(" ");
        for (String line : headers) {
            if (!line.contains(":")) continue;
            final String[] parts = line.split(":");
            map.put(parts[0], parts[1].trim());
        }
    }


    public String url() {
        return meta[1];
    }

    public String method() {
        return meta[0];
    }

    public String authToken() {
        return map.get("Authorization").trim();
    }
}
