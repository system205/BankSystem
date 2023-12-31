package oop.course.client.responses;


import java.util.*;
import java.util.regex.*;

public final class TransactionsResponse implements Response {
    private final Response response;

    public TransactionsResponse(Response response) {
        this.response = response;
    }

    public List<List<String>> transactions() {
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
        return transactions;
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
