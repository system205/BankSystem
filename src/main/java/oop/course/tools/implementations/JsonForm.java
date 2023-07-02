package oop.course.tools.implementations;

import oop.course.tools.interfaces.*;

import java.math.*;
import java.util.regex.*;

public class JsonForm implements Form {
    private final String source;
    private static final String REGEX = "\"%s\" *: *\"[^\"]*\"";
    private static final String STRING_REGEX = "\"[^\"]*\"$";
    private static final String INTEGER_REGEX = "\"\\d*\"$";

    public JsonForm(String source) {
        this.source = source;
    }

    public JsonForm(Iterable<String> payload) {
        StringBuilder data = new StringBuilder();
        for (String line : payload) {
            data.append(line);
        }
        this.source = data.toString();
    }

    @Override
    public long longField(String field) {
        Matcher matcher = Pattern
                .compile(String.format(REGEX, field))
                .matcher(this.source);
        if (matcher.find()) {
            Matcher m = Pattern
                    .compile(INTEGER_REGEX)
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
    public String stringField(String field) {
        Matcher matcher = Pattern
                .compile(String.format(REGEX, field))
                .matcher(this.source);
        if (matcher.find()) {
            Matcher m = Pattern
                    .compile(STRING_REGEX)
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

    @Override
    public BigDecimal bigDecimalField(String field) {
        return new BigDecimal(stringField(field));
    }
}
