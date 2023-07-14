package oop.course.interfaces;

import oop.course.tools.*;

import java.math.*;

public interface Transaction extends JSON {
    BigDecimal balanceChange();
}
