package oop.course.storage;

import java.util.*;

public interface Entity<T> {
    Optional<T> read(long id);
    void write(T object);
}
