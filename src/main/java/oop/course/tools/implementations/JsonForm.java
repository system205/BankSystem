package oop.course.tools.implementations;

import oop.course.tools.interfaces.*;

import java.util.regex.*;

public class JsonForm implements Form {
    private final String source;
    private final String regex;
    private final String stringRegex;
    private final String integerRegex;

    public JsonForm(String source) {
        this.source = source;
        this.regex = "\"%s\" *: *\"[^\"]*\"";
        this.stringRegex = "\"[^\"]*\"$";
        this.integerRegex = "\"[0-9]*\"$";
    }

    @Override
    public long extractLong(String field) {
        Matcher matcher = Pattern
                .compile(String.format(regex, field))
                .matcher(this.source);
        if (matcher.find()) {
            Matcher m = Pattern
                    .compile(this.integerRegex)
                    .matcher(matcher.group());
            if (m.find()) {
                final String value = m.group();
                return Long.parseLong(value.substring(1, value.length() - 1));
            } else {
                System.err.println("No number was found. JsonMalformed.");
                throw new RuntimeException("No number was found in field: " + field + ". JsonMalformed.");
            }
        } else {
            System.err.println("Json can't find field " + field);
            throw new RuntimeException("There is no attribute: " + field + " in JSON.");
        }
    }

    @Override
    public String extractString(String field) {
        Matcher matcher = Pattern
                .compile(String.format(regex, field))
                .matcher(this.source);
        if (matcher.find()) {
            Matcher m = Pattern
                    .compile(this.stringRegex)
                    .matcher(matcher.group());
            if (m.find()) {
                final String value = m.group();
                return value.substring(1, value.length() - 1);
            } else {
                System.err.println("No string was found. JsonMalformed.");
                throw new RuntimeException("No string value was found in field: " + field + ". JsonMalformed.");
            }
        } else {
            System.err.println("Json can't find field " + field);
            throw new RuntimeException("There is no attribute: " + field + " in JSON.");
        }
    }
}
