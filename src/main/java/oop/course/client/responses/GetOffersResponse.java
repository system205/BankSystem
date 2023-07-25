package oop.course.client.responses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GetOffersResponse implements Response {
    private final Response response;

    public GetOffersResponse(Response response) {
        this.response = response;
    }

    public List<List<String>> offers() {
        List<List<String>> offers = new ArrayList<>();
        var basicPattern = "\" *: *\"(.*?)\"";
        Pattern patternId = Pattern.compile("\"" + "id" + basicPattern);
        Pattern patternEmail = Pattern.compile("\"" + "customerEmail" + basicPattern);
        Pattern patternStatus = Pattern.compile("\"" + "status" + basicPattern);
        Pattern patternDate = Pattern.compile("\"" + "date" + basicPattern);
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.body());
        while (matcher.find()) {
            var offer = new String[4];
            var total = matcher.group(0);
            if (!total.contains("id")) {
                continue;
            }
            var matcher2 = patternId.matcher(total);
            if (matcher2.find()) {
                offer[0] = matcher2.group(1);
            }
            matcher2 = patternEmail.matcher(total);
            if (matcher2.find()) {
                offer[1] = matcher2.group(1);
            }
            matcher2 = patternStatus.matcher(total);
            if (matcher2.find()) {
                offer[2] = matcher2.group(1);
            }
            matcher2 = patternDate.matcher(total);
            if (matcher2.find()) {
                offer[3] = matcher2.group(1);
            }
            offers.add(Arrays.stream(offer).toList());
        }
        return offers;
    }

    @Override
    public boolean isSuccess() {
        return response.isSuccess();
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }

    @Override
    public String message() {
        return response.message();
    }

    @Override
    public String value(String key) {
        return response.value(key);
    }

    @Override
    public String[] values(String key) {
        return response.values(key);
    }

    @Override
    public String body() {
        return response.body();
    }
}
