package oop.course.client.responses;

import oop.course.client.gui.TerminalOffersTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetOffersResponse implements Response {
    private final BasicResponse response;

    public GetOffersResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }

    public TerminalOffersTable offersTable(Consumer<List<String>> selectAction) {
        List<List<String>> offers = new ArrayList<>();
        var basicPattern = "\" *: *\"(.*?)\"";
        Pattern patternId = Pattern.compile("\"" + "id" + basicPattern);
        Pattern patternEmail = Pattern.compile("\"" + "customerEmail" + basicPattern);
        Pattern patternStatus = Pattern.compile("\"" + "status" + basicPattern);
        Pattern patternDate = Pattern.compile("\"" + "date" + basicPattern);
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.raw());
        while (matcher.find()) {
            var offer = new String[4];
            var total = matcher.group(0);
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
        return new TerminalOffersTable(offers, selectAction);
    }
}
