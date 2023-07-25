package oop.course.entity.transaction;

import oop.course.miscellaneous.*;

import java.math.*;

public interface Transaction extends JSON {
    BigDecimal balanceChange();
}
