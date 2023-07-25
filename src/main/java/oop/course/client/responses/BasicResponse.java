package oop.course.client.responses;

import java.util.*;
import java.util.regex.*;

public final class BasicResponse implements Response {
    private final String response;

    public BasicResponse(String response) {
        this.response = response;
    }

    @Override
    public boolean isSuccess() {
        return statusCode() >= 200 && statusCode() < 300;
    }

    @Override
    public String message() {
        return value("message");
    }

    @Override
    public String value(String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\" *: *\"((.|\n)*?)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return "The client uses deprecated APIs, this response is not expected from the server";
        }
    }

    @Override
    public String[] values(String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\" *: *\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);
        List<String> res = new ArrayList<>();
        while (matcher.find()) {
            res.add(matcher.group(1));
        }
        return res.toArray(new String[0]);
    }

    @Override
    public int statusCode() {
        var strings = response.split("\n");
        try {
            return Integer.parseInt(strings[0].split(" ")[1]);
        } catch (NumberFormatException exception) {
            return 500;
        }
    }

    @Override
    public String body() {
        var regex = Pattern.compile("\\{(?s).*\\}");
        var matcher = regex.matcher(response);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
