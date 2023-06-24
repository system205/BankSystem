package oop.course.storage;

public interface Database<T> {
    T read(long id);
    void write(T object);
}
