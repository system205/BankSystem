package oop.course.client.responses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatementResponse implements Response {
    private final Response response;

    public StatementResponse(Response response) {
        this.response = response;
    }

    public <T> T fillTransactionsTable(Function<List<List<String>>, T> renderer) {
        List<List<String>> transactions = new ArrayList<>();

        Pattern patternType = Pattern.compile("\"" + "type" + "\" *: *\"(.*?)\"");
        Pattern patternFrom = Pattern.compile("\"" + "from" + "\" *: *\"(.*?)\"");
        Pattern patternAmount = Pattern.compile("\"" + "amount" + "\" *: *\"(.*?)\"");
        Pattern patternDate = Pattern.compile("\"" + "date" + "\" *: *\"(.*?)\"");
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.body());
        while (matcher.find()) {
            var curTrans = new String[]{"", "request", "", ""};
            var trans = matcher.group(0);
            if (!trans.contains("type")) {
                continue;
            }
            var matcher2 = patternType.matcher(trans);
            if (matcher2.find()) {
                curTrans[0] = matcher2.group(1);
            }
            matcher2 = patternFrom.matcher(trans);
            if (matcher2.find()) {
                curTrans[1] = matcher2.group(1);
            }
            matcher2 = patternAmount.matcher(trans);
            if (matcher2.find()) {
                curTrans[2] = matcher2.group(1);
            }
            matcher2 = patternDate.matcher(trans);
            if (matcher2.find()) {
                curTrans[3] = matcher2.group(1);
            }
            transactions.add(Arrays.stream(curTrans).toList());
        }
        return renderer.apply(transactions);
    }

    public String startingBalance() {
        return response.value("startingBalance");
    }

    public String endingBalance() {
        return response.value("endingBalance");
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
