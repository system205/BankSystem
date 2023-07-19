package oop.course.client.responses;

import oop.course.client.gui.TerminalTransactionTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatementResponse implements Response {
    private final BasicResponse response;

    public StatementResponse(BasicResponse response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return !Objects.equals(response.raw(), "");
    }

    public TerminalTransactionTable transactionsTable() {
        List<List<String>> transactions = new ArrayList<>();

        Pattern patternType = Pattern.compile("\"" + "type" + "\" *: *\"(.*?)\"");
        Pattern patternFrom = Pattern.compile("\"" + "from" + "\" *: *\"(.*?)\"");
        Pattern patternAmount = Pattern.compile("\"" + "amount" + "\" *: *\"(.*?)\"");
        Pattern patternDate = Pattern.compile("\"" + "date" + "\" *: *\"(.*?)\"");
        Pattern main = Pattern.compile("\\{(.|\\n)*?\\}");

        Matcher matcher = main.matcher(response.raw());
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
        return new TerminalTransactionTable(transactions);
    }

    public String startingBalance() {
        return response.value("startingBalance");
    }

    public String endingBalance() {
        return response.value("endingBalance");
    }
}
