package oop.course.auth;

import java.util.List;
import java.util.Map;

/**
 * Class that for each url says whether the user is allowed to access it
 * По идее, мне нужен процесс, который выбросит исключение, если текущий пользователь не может зайти по текущему юрл
 * Так же нужно для каждого юрл знать, какие роли имеют к ней доступ.
 * Что если создать класс GuardedUrl, который выбросит исключение, если юзер не может по ней пройти.
 */
public class RolesConfiguration {
    /**
     * Map that contains all urls that are intended for specific roles.
     * If a url is for everyone, then it should not be here.
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
