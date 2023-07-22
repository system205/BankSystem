package oop.course.client.responses;

import oop.course.client.gui.TerminalAutoPaymentsTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListAutoPaymentsResponse implements Response {
    private final Response response;

    public ListAutoPaymentsResponse(Response response) {
        this.response = response;
    }

    public TerminalAutoPaymentsTable autoPayments(Consumer<List<String>> selectAction) {
        List<List<String>> autoPayments = new ArrayList<>();
        var basicPattern = "\" *: *\"(.*?)\"";
        Pattern patternId = Pattern.compile("\"" + "id" + basicPattern);
        Pattern patternSender = Pattern.compile("\"" + "sender" + basicPattern);
        Pattern patternReceiver = Pattern.compile("\"" + "receiver" + basicPattern);
        Pattern patternAmount = Pattern.compile("\"" + "amount" + basicPattern);
        Pattern patternDate = Pattern.compile("\"" + "startDate" + basicPattern);
        Pattern patternPeriod = Pattern.compile("\"" + "period" + basicPattern);
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.body());
        while (matcher.find()) {
            var autopayment = new String[6];
            var total = matcher.group(0);
            var matcher2 = patternId.matcher(total);
            if (matcher2.find()) {
                autopayment[0] = matcher2.group(1);
            }
            matcher2 = patternSender.matcher(total);
            if (matcher2.find()) {
                autopayment[1] = matcher2.group(1);
            }
            matcher2 = patternReceiver.matcher(total);
            if (matcher2.find()) {
                autopayment[2] = matcher2.group(1);
            }
            matcher2 = patternAmount.matcher(total);
            if (matcher2.find()) {
                autopayment[3] = matcher2.group(1);
            }
            matcher2 = patternDate.matcher(total);
            if (matcher2.find()) {
                autopayment[4] = matcher2.group(1);
            }
            matcher2 = patternPeriod.matcher(total);
            if (matcher2.find()) {
                autopayment[5] = matcher2.group(1);
            }
            autoPayments.add(Arrays.stream(autopayment).toList());
        }
        return new TerminalAutoPaymentsTable(autoPayments, selectAction);
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
