package oop.course.auth;

import java.util.List;
import java.util.Map;

public class RolesConfiguration {
    private final Map<String, List<String>> roles;
    public RolesConfiguration(Map<String, List<String>> roles) {
        this.roles = roles;
    }

    public boolean isAllowedToGo(String role, String url) {
        return roles.containsKey(role) && roles.get(role).contains(url);
    }
}
