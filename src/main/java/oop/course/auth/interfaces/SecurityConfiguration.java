package oop.course.auth.interfaces;

import java.util.Collection;

public interface SecurityConfiguration {
    boolean isAccessibleUrl(String url);

    boolean isValidToken(Collection<String> headers, String url);
}
