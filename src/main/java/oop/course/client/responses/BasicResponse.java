package oop.course.client.responses;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasicResponse implements Response {
    private final String response;

    public BasicResponse(String response) {
        this.response = response;
    }

    public String value(String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\": ?\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);
        if (matcher.find()) {
            return matcher.group(1);
        }
        else {
            throw new RuntimeException("The field was not present in the response");
        }
    }

    public List<String> values(String key) {
        Pattern pattern = Pattern.compile("\"" + key + "\" *: *\"(.*?)\"");
        Matcher matcher = pattern.matcher(response);
        List<String> res = new ArrayList<>();
        while (matcher.find()) {
            res.add(matcher.group(1));
        }
        return res;
    }

    public String raw() {
        return response;
    }
}
