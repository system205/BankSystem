package oop.course.client.responses;

import oop.course.client.AutoPayment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListAutoPaymentsResponse implements Response {
    private final BasicResponse response;

    public ListAutoPaymentsResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }

    public List<AutoPayment> autoPayments() {
        List<AutoPayment> autoPayments = new ArrayList<>();
        var basicPattern = "\" *: *\"(.*?)\"";
        Pattern patternId = Pattern.compile("\"" + "id" + basicPattern);
        Pattern patternSender = Pattern.compile("\"" + "sender" + basicPattern);
        Pattern patternReceiver = Pattern.compile("\"" + "receiver" + basicPattern);
        Pattern patternAmount = Pattern.compile("\"" + "amount" + basicPattern);
        Pattern patternDate = Pattern.compile("\"" + "startDate" + basicPattern);
        Pattern patternPeriod = Pattern.compile("\"" + "period" + basicPattern);
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.raw());
        while (matcher.find()) {
            var autopayment = new AutoPayment("", "", "", "", "", "");
            var total = matcher.group(0);
            var matcher2 = patternId.matcher(total);
            if (matcher2.find()) {
                autopayment = new AutoPayment(matcher2.group(1), autopayment.sender(), autopayment.receiver(),
                        autopayment.amount(), autopayment.startDate(), autopayment.period());
            }
            matcher2 = patternSender.matcher(total);
            if (matcher2.find()) {
                autopayment = new AutoPayment(autopayment.id(), matcher2.group(1), autopayment.receiver(),
                        autopayment.amount(), autopayment.startDate(), autopayment.period());
            }
            matcher2 = patternReceiver.matcher(total);
            if (matcher2.find()) {
                autopayment = new AutoPayment(autopayment.id(), autopayment.sender(), matcher2.group(1),
                        autopayment.amount(), autopayment.startDate(), autopayment.period());
            }
            matcher2 = patternAmount.matcher(total);
            if (matcher2.find()) {
                autopayment = new AutoPayment(autopayment.id(), autopayment.sender(), autopayment.receiver(),
                        matcher2.group(1), autopayment.startDate(), autopayment.period());
            }
            matcher2 = patternDate.matcher(total);
            if (matcher2.find()) {
                autopayment = new AutoPayment(autopayment.id(), autopayment.sender(), autopayment.receiver(),
                        autopayment.amount(), matcher2.group(1), autopayment.period());
            }
            matcher2 = patternPeriod.matcher(total);
            if (matcher2.find()) {
                autopayment = new AutoPayment(autopayment.id(), autopayment.sender(), autopayment.receiver(),
                        autopayment.amount(), autopayment.startDate(), matcher2.group(1));
            }
            autoPayments.add(autopayment);
        }
        return autoPayments;
    }

    @Override
    public int statusCode() {
        return response.statusCode();
    }
}
