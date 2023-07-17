package oop.course.client.responses;

import oop.course.client.Offer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public List<Offer> offers() {
        List<Offer> offers = new ArrayList<>();
        var basicPattern = "\" *: *\"(.*?)\"";
        Pattern patternId = Pattern.compile("\"" + "id" + basicPattern);
        Pattern patternEmail = Pattern.compile("\"" + "customerEmail" + basicPattern);
        Pattern patternStatus = Pattern.compile("\"" + "status" + basicPattern);
        Pattern patternDate = Pattern.compile("\"" + "date" + basicPattern);
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.raw());
        while (matcher.find()) {
            var offer = new Offer("", "", "", "");
            var total = matcher.group(0);
            var matcher2 = patternId.matcher(total);
            if (matcher2.find()) {
                offer = new Offer(matcher2.group(1), offer.email(), offer.status(), offer.date());
            }
            matcher2 = patternEmail.matcher(total);
            if (matcher2.find()) {
                offer = new Offer(offer.id(), matcher2.group(1), offer.status(), offer.date());
            }
            matcher2 = patternStatus.matcher(total);
            if (matcher2.find()) {
                offer = new Offer(offer.id(), offer.email(), matcher2.group(1), offer.date());
            }
            matcher2 = patternDate.matcher(total);
            if (matcher2.find()) {
                offer = new Offer(offer.id(), offer.email(), offer.status(), matcher2.group(1));
            }
            offers.add(offer);
        }
        return offers;
    }
}
