package oop.course.auth;

import java.util.*;

/**
 * Class that for each url says whether the user is allowed to access it
 */
public class RolesConfiguration {
    /**
     * Map that contains all urls that are intended for specific roles.
     * If an url is for everyone, then it should not be here.
     */
    private final Map<String, List<String>> allowedRoles;

    public RolesConfiguration(Map<String, List<String>> allowedRoles) {
        this.allowedRoles = allowedRoles;
    }

    public boolean isAllowedToGo(String url, String role) {
        String baseUrl = "/" + url.split("/")[1];
        return !allowedRoles.containsKey(baseUrl) || allowedRoles.get(baseUrl).contains(role);
    }

    public boolean isAllowedToGo(String url) {
        String baseUrl = "/" + url.split("/")[1];
        return !allowedRoles.containsKey(baseUrl);
    }
}
