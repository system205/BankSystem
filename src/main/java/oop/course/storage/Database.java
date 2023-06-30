package oop.course.storage;

public interface Database<I, T> {
    T read(I id);

    void write(T object);
}
