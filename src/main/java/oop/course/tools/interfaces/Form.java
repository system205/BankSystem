package oop.course.tools.interfaces;

import oop.course.exceptions.MalformedDataException;

import java.math.*;

public interface Form {
    long longField(String field) throws MalformedDataException;

    String stringField(String field) throws MalformedDataException;

    BigDecimal bigDecimalField(String field) throws MalformedDataException;
}
