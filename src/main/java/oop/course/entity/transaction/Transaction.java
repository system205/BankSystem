package oop.course.entity.transaction;

import oop.course.tools.*;

import java.math.*;

public interface Transaction extends JSON {
    BigDecimal balanceChange();
}
