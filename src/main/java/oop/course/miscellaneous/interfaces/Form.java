package oop.course.miscellaneous.interfaces;

import java.math.*;

public interface Form {
    long longField(String field) throws Exception;

    String stringField(String field) throws Exception;

    BigDecimal bigDecimalField(String field) throws Exception;
}
