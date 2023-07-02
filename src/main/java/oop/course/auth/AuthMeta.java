package oop.course.auth;

import oop.course.interfaces.Request;

import java.util.HashMap;
import java.util.Map;

public class AuthMeta {
    private final Request request;

    private final Map<String, String> map = new HashMap<>();

    public AuthMeta(Request request) {
        this.request = request;
        processHeaders();
    }

    private void processHeaders() {
        for (String line : request.headers()) {
            if (!line.contains(":")) continue;
            final String[] parts = line.split(":");
            map.put(parts[0], parts[1].trim());
        }
    }

    public String authToken() {
        return map.get("Authorization").trim();
    }
}
