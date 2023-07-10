package oop.course.tools.implementations;

import oop.course.exceptions.MalformedDataException;
import oop.course.tools.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.*;
import java.util.regex.*;

public class JsonForm implements Form {
    private final Logger log = LoggerFactory.getLogger(JsonForm.class);
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
    public long longField(String field) throws MalformedDataException {
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
                log.error("No number was found. JsonMalformed.");
                throw new MalformedDataException("No number was found in field: " + field + ". JsonMalformed.");
            }
        } else {
            log.error("Json can't find field " + field);
            throw new MalformedDataException("There is no attribute: " + field + " in JSON.");
        }
    }

    @Override
    public String stringField(String field) throws MalformedDataException {
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
                log.error("No string was found. JsonMalformed.");
                throw new MalformedDataException("No string value was found in field: " + field + ". JsonMalformed.");
            }
        } else {
            log.error("Json can't find field " + field);
            throw new MalformedDataException("There is no attribute: " + field + " in JSON.");
        }
    }

    @Override
    public BigDecimal bigDecimalField(String field) throws MalformedDataException {
        return new BigDecimal(stringField(field));
    }
}
